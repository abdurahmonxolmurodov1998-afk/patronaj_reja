package com.patronaj.reja.repository

import com.patronaj.reja.data.dao.PatientDao
import com.patronaj.reja.data.entity.Patient
import kotlinx.coroutines.flow.Flow

class PatientRepository(private val dao: PatientDao) {

    fun getAll(): Flow<List<Patient>> = dao.getAll()
    fun getActive(): Flow<List<Patient>> = dao.getActive()
    fun search(query: String): Flow<List<Patient>> = dao.search(query)

    suspend fun getById(id: Long): Patient? = dao.getById(id)

    suspend fun save(patient: Patient): Long =
        if (patient.id == 0L) dao.insert(patient) else { dao.update(patient); patient.id }

    suspend fun delete(patient: Patient) = dao.delete(patient)

    /** MedID yoki JSHSHIR bo'yicha duplikat borligini tekshiradi (Excel import uchun ham foydali). */
    suspend fun isDuplicate(jshshir: String, medId: String, excludeId: Long = 0): Boolean {
        val byJshshir = if (jshshir.isNotBlank()) dao.countByJshshir(jshshir) else 0
        val byMedId = if (medId.isNotBlank()) dao.countByMedId(medId) else 0
        return (byJshshir + byMedId) > 0
    }
}
