package com.patronaj.reja.data.entity

enum class Gender {
    ERKAK, AYOL
}

/** Standart uchun jins filtri: ALL - ikkalasiga ham tegishli */
enum class GenderFilter {
    ALL, ERKAK, AYOL
}

enum class DGroup {
    NONE, D1, D2, D3, D4
}

/** Asosiy kontingent turlari (yosh bo'yicha guruhlar) */
enum class Contingent {
    C0_1,
    C1_3,
    C3_5,
    C6_18,
    C15_49,
    C30_44,
    C45_65,
    C40_PLUS
}

/** Standart nimaga qarab qo'llanilishini bildiradi */
enum class TargetType {
    CONTINGENT, D_GROUP
}

enum class Priority {
    HIGH, MEDIUM, LOW
}

enum class TaskStatus {
    PLANNED, COMPLETED, POSTPONED, MISSED
}
