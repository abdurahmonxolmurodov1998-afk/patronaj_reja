package com.patronaj.reja.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patronaj.reja.data.entity.Task
import com.patronaj.reja.data.entity.TaskStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE plannedDate = :date ORDER BY priority ASC")
    fun getForDate(date: LocalDate): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE plannedDate = :date")
    suspend fun getForDateOnce(date: LocalDate): List<Task>

    @Query("""
        SELECT * FROM tasks
        WHERE patientId = :patientId AND standardId = :standardId AND status = 'COMPLETED'
        ORDER BY completedDate DESC LIMIT 1
    """)
    suspend fun getLastCompleted(patientId: Long, standardId: Long): Task?

    @Query("""
        SELECT COUNT(*) FROM tasks
        WHERE patientId = :patientId AND standardId = :standardId
        AND status IN ('PLANNED', 'POSTPONED') AND plannedDate >= :fromDate
    """)
    suspend fun countOpenTasks(patientId: Long, standardId: Long, fromDate: LocalDate): Int

    @Query("SELECT * FROM tasks WHERE patientId = :patientId ORDER BY plannedDate DESC")
    fun getForPatient(patientId: Long): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(tasks: List<Task>)

    @Update
    suspend fun update(task: Task)

    @Query("SELECT COUNT(*) FROM tasks WHERE plannedDate = :date AND status = :status")
    fun countForDateByStatus(date: LocalDate, status: TaskStatus): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE plannedDate = :date")
    fun countForDate(date: LocalDate): Flow<Int>

    @Query("SELECT COUNT(*) FROM tasks WHERE plannedDate = :date AND taskType = :taskType")
    fun countForDateByType(date: LocalDate, taskType: String): Flow<Int>
}
