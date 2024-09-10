package com.example.healthapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity
@TypeConverters(SymptomConverter::class)
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val heartRate: Double,
    val respiratoryRate: Double,
    val symptoms: Map<String, Int>
)

class SymptomConverter {
    @TypeConverter
    fun fromMap(symptoms: Map<String, Int>): String {
        return symptoms.entries.joinToString(",") { "${it.key}:${it.value}" }
    }

    @TypeConverter
    fun toMap(data: String): Map<String, Int> {
        return data.split(",").associate {
            val (key, value) = it.split(":")
            key to value.toInt()
        }
    }
}
