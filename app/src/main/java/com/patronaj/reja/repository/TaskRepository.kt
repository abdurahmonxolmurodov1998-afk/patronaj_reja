package com.patronaj.reja.repository

import com.patronaj.reja.data.dao.PatientDao
import com.patronaj.reja.data.dao.StandardDao
import com.patronaj.reja.data.dao.TaskDao
import com.patronaj.reja.data.entity.Task
import com.patronaj.reja.data.entity.TaskStatus
import com.patronaj.reja.logic.TaskGenerator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.time.LocalDate

class TaskRepository(
    private val taskDao: TaskDao,
    private val patientDao: PatientDao,
    private val standardDao: StandardDao
) {
    private val generator = TaskGenerator(taskDao)

    fun getForDate(date: LocalDate): Flow<List<Task>> = taskDao.getForDate(date)

    fun getForPatient(patientId: Long): Flow<List<Task>> = taskDao.getForPatient(patientId)

    fun countForDate(date: LocalDate): Flow<Int> = taskDao.countForDate(date)

    fun countForDateByStatus(date: LocalDate, status: TaskStatus): Flow<Int> =
        taskDao.countForDateByStatus(date, status)

    fun countForDateByType(date: LocalDate, taskType: String): Flow<Int> =
        taskDao.countForDateByType(date, taskType)

    /** Berilgan sana (odatda bugun) uchun rejani standartlar asosida avtomatik generatsiya qiladi. */
    suspend fun ensurePlanFor(date: LocalDate = LocalDate.now()) {
        val activePatients = patientDao.getActive().first()
        val activeStandards = standardDao.getActiveOnce()
        generator.generateForToday(activePatients, activeStandards, date)
    }

    suspend fun updateStatus(task: Task, status: TaskStatus, completedDate: LocalDate? = null) {
        taskDao.update(task.copy(status = status, completedDate = completedDate))
    }
}
