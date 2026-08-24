package com.patronaj.reja.repository

import com.patronaj.reja.data.dao.StandardDao
import com.patronaj.reja.data.entity.Standard
import kotlinx.coroutines.flow.Flow

class StandardRepository(private val dao: StandardDao) {

    fun getAll(): Flow<List<Standard>> = dao.getAll()

    suspend fun getActiveOnce(): List<Standard> = dao.getActiveOnce()

    suspend fun getById(id: Long): Standard? = dao.getById(id)

    suspend fun save(standard: Standard): Long =
        if (standard.id == 0L) dao.insert(standard) else { dao.update(standard); standard.id }

    suspend fun delete(standard: Standard) = dao.delete(standard)
}
