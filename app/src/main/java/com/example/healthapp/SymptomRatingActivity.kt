package com.example.healthapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.launch

class SymptomRatingActivity : AppCompatActivity() {

    private lateinit var symptomAdapter: SymptomAdapter
    private val symptoms = listOf(
        Symptom("Nausea"),
        Symptom("Headache"),
        Symptom("Fever"),
        Symptom("Sore Throat"),
        Symptom("Cough"),
        Symptom("Shortness of Breath"),
        Symptom("Muscle Ache"),
        Symptom("Loss of Taste"),
        Symptom("Feeling Tired"),
        Symptom("Dizziness")
    )

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_rating)
        Log.d("SymptomRatingActivity", "onCreate: Activity started")

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "health-database"
        ).build()
        Log.d("SymptomRatingActivity", "onCreate: Database initialized")

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        symptomAdapter = SymptomAdapter(symptoms)
        recyclerView.adapter = symptomAdapter
        Log.d("SymptomRatingActivity", "onCreate: RecyclerView and Adapter set up")

        val uploadButton: Button = findViewById(R.id.upload_button)
        uploadButton.setOnClickListener {
            Log.d("SymptomRatingActivity", "Upload button clicked")
            saveDataToDatabase()
        }
    }

    private fun saveDataToDatabase() {
        val symptomRatings = symptoms.associate { it.name to it.rating }
        val heartRate = 72.0 // Replace with actual heart rate
        val respiratoryRate = 16.0 // Replace with actual respiratory rate

        Log.d("SymptomRatingActivity", "saveDataToDatabase: Preparing data to save")
        Log.d("SymptomRatingActivity", "HeartRate: $heartRate, RespiratoryRate: $respiratoryRate, Symptoms: $symptomRatings")

        val healthData = HealthData(
            heartRate = heartRate,
            respiratoryRate = respiratoryRate,
            symptoms = symptomRatings
        )

        lifecycleScope.launch {
            db.healthDataDao().insert(healthData)
            Log.d("SymptomRatingActivity", "Data saved to database successfully")
        }
    }
}