package com.patronaj.reja.data

import com.patronaj.reja.data.entity.Contingent
import com.patronaj.reja.data.entity.DGroup
import com.patronaj.reja.data.entity.GenderFilter
import com.patronaj.reja.data.entity.Priority
import com.patronaj.reja.data.entity.Standard
import com.patronaj.reja.data.entity.TargetType

/**
 * Ilova birinchi marta ishga tushganda avtomatik kiritiladigan standart normalar.
 * Foydalanuvchi Standartlar bo'limida bularni istalgancha o'zgartirishi,
 * o'chirishi yoki yangilarini qo'shishi mumkin.
 */
object DefaultStandards {

    fun list(): List<Standard> = listOf(
        Standard(
            name = "0-1 yosh patronaji",
            taskType = "PATRONAJ",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C0_1,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 30,
            priority = Priority.HIGH
        ),
        Standard(
            name = "1-3 yosh profilaktik ko'rigi",
            taskType = "PROFILAKTIK_KORIK",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C1_3,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 180,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "3-5 yosh profilaktik ko'rigi",
            taskType = "PROFILAKTIK_KORIK",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C3_5,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 365,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "6-18 yosh profilaktik ko'rigi",
            taskType = "PROFILAKTIK_KORIK",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C6_18,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 365,
            priority = Priority.LOW
        ),
        Standard(
            name = "15-49 yosh ayollar skriningi",
            taskType = "SKRINING",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C15_49,
            genderFilter = GenderFilter.AYOL,
            periodicityDays = 365,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "30-44 yosh skrining",
            taskType = "SKRINING",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C30_44,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 365,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "45-65 yosh profilaktik ko'rigi",
            taskType = "PROFILAKTIK_KORIK",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C45_65,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 365,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "40+ yosh skrining",
            taskType = "SKRINING",
            targetType = TargetType.CONTINGENT,
            contingent = Contingent.C40_PLUS,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 365,
            priority = Priority.LOW
        ),
        Standard(
            name = "D1 nazorati",
            taskType = "D_NAZORAT",
            targetType = TargetType.D_GROUP,
            dGroup = DGroup.D1,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 180,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "D2 nazorati",
            taskType = "D_NAZORAT",
            targetType = TargetType.D_GROUP,
            dGroup = DGroup.D2,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 90,
            priority = Priority.MEDIUM
        ),
        Standard(
            name = "D3 nazorati",
            taskType = "D_NAZORAT",
            targetType = TargetType.D_GROUP,
            dGroup = DGroup.D3,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 60,
            priority = Priority.HIGH
        ),
        Standard(
            name = "D4 nazorati",
            taskType = "D_NAZORAT",
            targetType = TargetType.D_GROUP,
            dGroup = DGroup.D4,
            genderFilter = GenderFilter.ALL,
            periodicityDays = 30,
            priority = Priority.HIGH
        )
    )
}
