package com.patronaj.reja.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Foydalanuvchi o'zi kiritadigan/o'zgartiradigan standart (norma).
 * Masalan: "0-1 yosh patronaji", davriyligi 30 kun, ustuvorligi HIGH.
 *
 * targetType = CONTINGENT bo'lsa -> [contingent] maydoni ishlatiladi (yosh guruhi bo'yicha).
 * targetType = D_GROUP bo'lsa    -> [dGroup] maydoni ishlatiladi (D1-D4 nazorati uchun).
 */
@Entity(tableName = "standards")
data class Standard(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val name: String,                          // standart nomi, masalan "0-1 patronaj"
    val taskType: String,                       // "PATRONAJ", "SKRINING", "PROFILAKTIK_KORIK", "D_NAZORAT" ...

    val targetType: TargetType = TargetType.CONTINGENT,
    val contingent: Contingent? = null,          // targetType=CONTINGENT bo'lsa
    val dGroup: DGroup? = null,                  // targetType=D_GROUP bo'lsa

    val genderFilter: GenderFilter = GenderFilter.ALL,

    val periodicityDays: Int,                    // necha kunda bir marta takrorlanadi

    val priority: Priority = Priority.MEDIUM,
    val isActive: Boolean = true
)
