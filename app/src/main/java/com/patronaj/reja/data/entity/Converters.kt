package com.patronaj.reja.data.entity

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {

    @TypeConverter
    fun fromEpochDay(value: Long?): LocalDate? = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun localDateToEpochDay(date: LocalDate?): Long? = date?.toEpochDay()

    @TypeConverter
    fun fromGender(value: String?): Gender? = value?.let { Gender.valueOf(it) }

    @TypeConverter
    fun genderToString(value: Gender?): String? = value?.name

    @TypeConverter
    fun fromGenderFilter(value: String?): GenderFilter? = value?.let { GenderFilter.valueOf(it) }

    @TypeConverter
    fun genderFilterToString(value: GenderFilter?): String? = value?.name

    @TypeConverter
    fun fromDGroup(value: String?): DGroup? = value?.let { DGroup.valueOf(it) }

    @TypeConverter
    fun dGroupToString(value: DGroup?): String? = value?.name

    @TypeConverter
    fun fromContingent(value: String?): Contingent? = value?.let { Contingent.valueOf(it) }

    @TypeConverter
    fun contingentToString(value: Contingent?): String? = value?.name

    @TypeConverter
    fun fromTargetType(value: String?): TargetType? = value?.let { TargetType.valueOf(it) }

    @TypeConverter
    fun targetTypeToString(value: TargetType?): String? = value?.name

    @TypeConverter
    fun fromPriority(value: String?): Priority? = value?.let { Priority.valueOf(it) }

    @TypeConverter
    fun priorityToString(value: Priority?): String? = value?.name

    @TypeConverter
    fun fromTaskStatus(value: String?): TaskStatus? = value?.let { TaskStatus.valueOf(it) }

    @TypeConverter
    fun taskStatusToString(value: TaskStatus?): String? = value?.name
}
