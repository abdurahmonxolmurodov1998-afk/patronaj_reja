package com.patronaj.reja.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.patronaj.reja.data.entity.Standard
import kotlinx.coroutines.flow.Flow

@Dao
interface StandardDao {

    @Query("SELECT * FROM standards ORDER BY name ASC")
    fun getAll(): Flow<List<Standard>>

    @Query("SELECT * FROM standards WHERE isActive = 1 ORDER BY priority ASC, name ASC")
    suspend fun getActiveOnce(): List<Standard>

    @Query("SELECT * FROM standards WHERE id = :id")
    suspend fun getById(id: Long): Standard?

    @Query("SELECT COUNT(*) FROM standards")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(standard: Standard): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(standards: List<Standard>)

    @Update
    suspend fun update(standard: Standard)

    @Delete
    suspend fun delete(standard: Standard)
}
