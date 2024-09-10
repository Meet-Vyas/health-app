package com.example.healthapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HealthDataDao {
    @Insert
    suspend fun insert(healthData: HealthData)

    @Query("SELECT * FROM HealthData")
    suspend fun getAll(): List<HealthData>
}
