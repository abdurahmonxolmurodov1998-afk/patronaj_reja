package com.patronaj.reja.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.Period

/**
 * Asosiy AHOLI bazasi. Barcha kontingentlar (0-1, 1-3, 15-49 va h.k.)
 * shu jadvaldan avtomatik hisoblanadi (ContingentEngine orqali) —
 * alohida saqlanmaydi.
 */
@Entity(tableName = "patients")
data class Patient(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val fullName: String,              // F.I.Sh.
    val birthDate: LocalDate,          // tug'ilgan sana
    val gender: Gender,                // jinsi
    val jshshir: String = "",          // JSHSHIR
    val medId: String = "",            // MedID
    val address: String = "",          // manzil
    val phone: String = "",            // telefon

    val dGroup: DGroup = DGroup.NONE,  // D1/D2/D3/D4 guruhi

    val isActive: Boolean = true       // faollik holati
) {
    /** Tug'ilgan sana asosida to'liq yil hisobida yosh. */
    fun ageYears(onDate: LocalDate = LocalDate.now()): Int =
        Period.between(birthDate, onDate).years

    /** Chaqaloqlar uchun oylik yosh (0-1 kontingentni aniq ko'rsatish uchun). */
    fun ageMonths(onDate: LocalDate = LocalDate.now()): Int {
        val p = Period.between(birthDate, onDate)
        return p.years * 12 + p.months
    }
}
