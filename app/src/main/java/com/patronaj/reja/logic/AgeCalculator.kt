package com.patronaj.reja.logic

import com.patronaj.reja.data.entity.Patient
import java.time.LocalDate

object AgeCalculator {

    /** Ekranda ko'rsatish uchun qulay yosh matni, masalan "7 oylik" yoki "34 yosh". */
    fun displayAge(patient: Patient, today: LocalDate = LocalDate.now()): String {
        val months = patient.ageMonths(today)
        return if (months < 12) {
            "$months oylik"
        } else {
            "${patient.ageYears(today)} yosh"
        }
    }
}
