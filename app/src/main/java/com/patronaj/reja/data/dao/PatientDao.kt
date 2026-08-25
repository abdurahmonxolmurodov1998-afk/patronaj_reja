package com.patronaj.reja.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patronaj.reja.data.entity.Patient
import kotlinx.coroutines.flow.Flow

@Dao
interface PatientDao {

    @Query("SELECT * FROM patients ORDER BY fullName ASC")
    fun getAll(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE isActive = 1 ORDER BY fullName ASC")
    fun getActive(): Flow<List<Patient>>

    @Query("SELECT * FROM patients WHERE id = :id")
    suspend fun getById(id: Long): Patient?

    @Query("""
        SELECT * FROM patients
        WHERE isActive = 1 AND (
            fullName LIKE '%' || :query || '%' OR
            jshshir LIKE '%' || :query || '%' OR
            medId LIKE '%' || :query || '%'
        )
        ORDER BY fullName ASC
    """)
    fun search(query: String): Flow<List<Patient>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(patient: Patient): Long

    @Update
    suspend fun update(patient: Patient)

    @Delete
    suspend fun delete(patient: Patient)

    @Query("SELECT COUNT(*) FROM patients WHERE jshshir = :jshshir AND jshshir != ''")
    suspend fun countByJshshir(jshshir: String): Int

    @Query("SELECT COUNT(*) FROM patients WHERE medId = :medId AND medId != ''")
    suspend fun countByMedId(medId: String): Int
}
