package com.patronaj.reja.logic

import com.patronaj.reja.data.entity.Contingent
import com.patronaj.reja.data.entity.Gender
import com.patronaj.reja.data.entity.Patient
import java.time.LocalDate

/**
 * Bemorning yoshi va jinsi asosida u qaysi kontingentlarga tegishli ekanligini aniqlaydi.
 * Bitta bemor bir vaqtning o'zida bir nechta kontingentga tegishli bo'lishi mumkin
 * (masalan 40 yoshli ayol: 30-44 va 40+ ikkalasiga ham tegishli).
 */
object ContingentEngine {

    fun contingentsFor(patient: Patient, today: LocalDate = LocalDate.now()): Set<Contingent> {
        val ageMonths = patient.ageMonths(today)
        val ageYears = patient.ageYears(today)
        val result = mutableSetOf<Contingent>()

        if (ageMonths < 12) result += Contingent.C0_1
        if (ageYears in 1..2) result += Contingent.C1_3
        if (ageYears in 3..4) result += Contingent.C3_5
        if (ageYears in 6..17) result += Contingent.C6_18
        if (patient.gender == Gender.AYOL && ageYears in 15..49) result += Contingent.C15_49
        if (ageYears in 30..44) result += Contingent.C30_44
        if (ageYears in 45..65) result += Contingent.C45_65
        if (ageYears >= 40) result += Contingent.C40_PLUS

        return result
    }

    fun label(c: Contingent): String = when (c) {
        Contingent.C0_1 -> "0-1 yosh"
        Contingent.C1_3 -> "1-3 yosh"
        Contingent.C3_5 -> "3-5 yosh"
        Contingent.C6_18 -> "6-18 yosh"
        Contingent.C15_49 -> "15-49 yosh (ayol)"
        Contingent.C30_44 -> "30-44 yosh"
        Contingent.C45_65 -> "45-65 yosh"
        Contingent.C40_PLUS -> "40+ yosh"
    }
}
