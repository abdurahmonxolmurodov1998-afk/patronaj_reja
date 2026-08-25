package com.patronaj.reja.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.patronaj.reja.data.dao.PatientDao
import com.patronaj.reja.data.dao.StandardDao
import com.patronaj.reja.data.dao.TaskDao
import com.patronaj.reja.data.entity.Converters
import com.patronaj.reja.data.entity.Patient
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.data.entity.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Patient::class, Standard::class, Task::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun patientDao(): PatientDao
    abstract fun standardDao(): StandardDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "patronaj_reja.db"
                ).addCallback(object : Callback() {
                    override fun onOpen(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Birinchi ochilishda standart normalarni avtomatik kiritish
                        CoroutineScope(Dispatchers.IO).launch {
                            val database = INSTANCE ?: return@launch
                            val dao = database.standardDao()
                            if (dao.count() == 0) {
                                dao.insertAll(DefaultStandards.list())
                            }
                        }
                    }
                }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
