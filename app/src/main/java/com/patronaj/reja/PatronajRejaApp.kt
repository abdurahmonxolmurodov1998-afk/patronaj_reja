package com.patronaj.reja

import android.app.Application
import com.patronaj.reja.data.AppDatabase
import com.patronaj.reja.repository.PatientRepository
import com.patronaj.reja.repository.StandardRepository
import com.patronaj.reja.repository.TaskRepository

class PatronajRejaApp : Application() {

    lateinit var database: AppDatabase
        private set

    lateinit var patientRepository: PatientRepository
        private set

    lateinit var standardRepository: StandardRepository
        private set

    lateinit var taskRepository: TaskRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = AppDatabase.getInstance(this)
        patientRepository = PatientRepository(database.patientDao())
        standardRepository = StandardRepository(database.standardDao())
        taskRepository = TaskRepository(database.taskDao(), database.patientDao(), database.standardDao())
    }
}
