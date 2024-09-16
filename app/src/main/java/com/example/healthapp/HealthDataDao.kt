package com.example.healthapp

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface HealthDataDao {
    @Insert
    suspend fun insert(healthData: HealthData)
}
