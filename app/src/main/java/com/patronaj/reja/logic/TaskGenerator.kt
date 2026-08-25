package com.patronaj.reja.logic

import com.patronaj.reja.data.dao.TaskDao
import com.patronaj.reja.data.entity.DGroup
import com.patronaj.reja.data.entity.Gender
import com.patronaj.reja.data.entity.GenderFilter
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.data.entity.TargetType
import com.patronaj.reja.data.entity.Task
import java.time.LocalDate

/**
 * BUGUNGI REJA generatori.
 *
 * Har bir faol bemor va har bir faol standart uchun:
 *  1. bemor shu standartga (kontingent yoki D-guruh bo'yicha) mos keladimi tekshiradi;
 *  2. oxirgi bajarilgan task sanasini tekshiradi;
 *  3. keyingi rejalashtirilgan sanani hisoblaydi (oxirgi bajarilgan + davriylik,
 *     yoki hech qachon bajarilmagan bo'lsa - bugundan boshlab darhol);
 *  4. agar keyingi sana bugun yoki undan oldin bo'lsa va bugun uchun
 *     hali ochiq task yaratilmagan bo'lsa - yangi Task yaratadi.
 */
class TaskGenerator(private val taskDao: TaskDao) {

    suspend fun generateForToday(
        patients: List<Patient>,
        standards: List<Standard>,
        today: LocalDate = LocalDate.now()
    ): List<Task> {
        val newTasks = mutableListOf<Task>()

        val activePatients = patients.filter { it.isActive }
        val activeStandards = standards.filter { it.isActive }

        for (patient in activePatients) {
            val contingents = ContingentEngine.contingentsFor(patient, today)

            for (standard in activeStandards) {
                if (!matches(patient, standard, contingents)) continue

                // Shu bemor + shu standart uchun bugun/kelajakda ochiq task bormi?
                val openCount = taskDao.countOpenTasks(patient.id, standard.id, today)
                if (openCount > 0) continue

                val lastCompleted = taskDao.getLastCompleted(patient.id, standard.id)
                val nextDueDate = lastCompleted?.completedDate?.plusDays(standard.periodicityDays.toLong())
                    ?: today // hech qachon bajarilmagan bo'lsa - darhol kerak

                if (!nextDueDate.isAfter(today)) {
                    newTasks += Task(
                        patientId = patient.id,
                        standardId = standard.id,
                        taskType = standard.taskType,
                        plannedDate = today,
                        priority = standard.priority,
                        reason = standard.name,
                        notes = null
                    )
                }
            }
        }

        if (newTasks.isNotEmpty()) {
            taskDao.insertAll(newTasks)
        }
        return newTasks
    }

    private fun matches(patient: Patient, standard: Standard, contingents: Set<com.patronaj.reja.data.entity.Contingent>): Boolean {
        val genderOk = when (standard.genderFilter) {
            GenderFilter.ALL -> true
            GenderFilter.ERKAK -> patient.gender == Gender.ERKAK
            GenderFilter.AYOL -> patient.gender == Gender.AYOL
        }
        if (!genderOk) return false

        return when (standard.targetType) {
            TargetType.CONTINGENT -> standard.contingent != null && standard.contingent in contingents
            TargetType.D_GROUP -> standard.dGroup != null && standard.dGroup != DGroup.NONE && patient.dGroup == standard.dGroup
        }
    }
}
