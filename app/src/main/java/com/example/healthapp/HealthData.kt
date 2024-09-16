package com.example.healthapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HealthData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val heartRate: Int,
    val respiratoryRate: Int,
    val nausea: Int,
    val headache: Int,
    val fever: Int,
    val soreThroat: Int,
    val cough: Int,
    val shortnessOfBreath: Int,
    val muscleAche: Int,
    val lossOfTaste: Int,
    val feelingTired: Int,
    val dizziness: Int
)