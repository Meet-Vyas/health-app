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
    private var heartRate = 0
    private var respiratoryRate = 0

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

        heartRate = intent.getIntExtra("HEART_RATE", 0)
        respiratoryRate = intent.getIntExtra("RESPIRATORY_RATE", 0)

        Log.d(
            "SymptomRatingActivity",
            "Received heartRate: $heartRate, respiratoryRate: $respiratoryRate"
        )

        val uploadButton: Button = findViewById(R.id.upload_button)
        uploadButton.setOnClickListener {
            Log.d("SymptomRatingActivity", "Upload button clicked")
            saveDataToDatabase()
        }
    }

    private fun saveDataToDatabase() {
        val healthData = HealthData(
            heartRate = heartRate,
            respiratoryRate = respiratoryRate,
            nausea = symptoms.find { it.name == "Nausea" }?.rating ?: 0,
            headache = symptoms.find { it.name == "Headache" }?.rating ?: 0,
            fever = symptoms.find { it.name == "Fever" }?.rating ?: 0,
            soreThroat = symptoms.find { it.name == "Sore Throat" }?.rating ?: 0,
            cough = symptoms.find { it.name == "Cough" }?.rating ?: 0,
            shortnessOfBreath = symptoms.find { it.name == "Shortness of Breath" }?.rating ?: 0,
            muscleAche = symptoms.find { it.name == "Muscle Ache" }?.rating ?: 0,
            lossOfTaste = symptoms.find { it.name == "Loss of Taste" }?.rating ?: 0,
            feelingTired = symptoms.find { it.name == "Feeling Tired" }?.rating ?: 0,
            dizziness = symptoms.find { it.name == "Dizziness" }?.rating ?: 0
        )

        lifecycleScope.launch {
            db.healthDataDao().insert(healthData)
            Log.d("SymptomRatingActivity", "Data saved to database successfully")
        }
    }
}