package com.patronaj.reja.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(entity = Patient::class, parentColumns = ["id"], childColumns = ["patientId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Standard::class, parentColumns = ["id"], childColumns = ["standardId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("patientId"), Index("standardId"), Index("plannedDate")]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val patientId: Long,
    val standardId: Long,

    val taskType: String,
    val plannedDate: LocalDate,
    val priority: Priority,
    val reason: String,

    val status: TaskStatus = TaskStatus.PLANNED,
    val completedDate: LocalDate? = null,
    val notes: String? = null
)
