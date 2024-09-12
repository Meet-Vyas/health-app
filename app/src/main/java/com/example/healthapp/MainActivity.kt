package com.example.healthapp

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.camera.video.VideoRecordEvent
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var viewFinder: PreviewView
    private lateinit var captureButton: Button
    private lateinit var resultTextView: TextView
    private lateinit var resultTextView2: TextView
    private lateinit var measureRespiratoryRateButton: Button
    private lateinit var symptomButton: Button
    private lateinit var cameraExecutor: ExecutorService

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private val accelValuesX = mutableListOf<Float>()
    private val accelValuesY = mutableListOf<Float>()
    private val accelValuesZ = mutableListOf<Float>()

    private var heartRate = 0
    private var respiratoryRate = 0

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        Log.d(TAG, "Permission result: $permissions")
        if (allPermissionsGranted()) {
            Log.d(TAG, "All permissions granted, starting camera")
            startCamera()
        } else {
            Log.e(TAG, "Permissions not granted by the user")
            Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate called")
        setContentView(R.layout.activity_main)

        viewFinder = findViewById(R.id.viewFinder)
        captureButton = findViewById(R.id.camera_capture_button)
        resultTextView = findViewById(R.id.resultTextView)
        resultTextView2 = findViewById(R.id.resultTextView2)
        measureRespiratoryRateButton = findViewById(R.id.measure_respiratory_rate_button)
        symptomButton = findViewById(R.id.symptom_button)

        captureButton.isEnabled = false

        if (allPermissionsGranted()) {
            Log.d(TAG, "All permissions already granted, starting camera")
            startCamera()
        } else {
            Log.d(TAG, "Requesting permissions")
            requestPermissions()
        }

        captureButton.setOnClickListener {
            Log.d(TAG, "Capture button clicked")
            captureVideo()
        }

        measureRespiratoryRateButton.setOnClickListener {
            startRespiratoryMeasurement()
        }

        symptomButton.setOnClickListener {
            Log.d(TAG, "Heart rate: $heartRate, Respiratory rate: $respiratoryRate")

            startActivity(Intent(this, SymptomRatingActivity::class.java).apply {
                putExtra("HEART_RATE", heartRate)
                putExtra("RESPIRATORY_RATE", respiratoryRate)
            })
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun startRespiratoryMeasurement() {
        accelValuesX.clear()
        accelValuesY.clear()
        accelValuesZ.clear()

        Log.d(TAG, "Starting respiratory measurement")
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        measureRespiratoryRateButton.postDelayed({
            sensorManager.unregisterListener(this)
            calculateRespiratoryRate()
        }, 45000)
    }

    private fun calculateRespiratoryRate() {
        Log.d(TAG, "Respiratory measurement stopped")
        respiratoryRate =
            RespiratoryRateCalculator.calculate(accelValuesX, accelValuesY, accelValuesZ)
        Log.d(TAG, "Calculated respiratory rate: $respiratoryRate")
        resultTextView2.text = "Respiratory Rate: $respiratoryRate breaths/min"
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            accelValuesX.add(it.values[0])
            accelValuesY.add(it.values[1])
            accelValuesZ.add(it.values[2])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // No action needed
    }

    private fun startCamera() {
        Log.d(TAG, "Starting camera")
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            Log.d(TAG, "CameraProvider obtained")

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, videoCapture
                )
                camera.cameraControl.enableTorch(true)
                captureButton.isEnabled = true
                Log.d(TAG, "Camera started successfully")
            } catch (exc: Exception) {
                Log.e(TAG, "Failed to start camera", exc)
                Toast.makeText(this, "Failed to start camera: ${exc.message}", Toast.LENGTH_SHORT)
                    .show()
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun captureVideo() {
        Log.d(TAG, "Capturing video")
        val videoCapture = this.videoCapture ?: return
        captureButton.isEnabled = false

        val curRecording = recording
        if (curRecording != null) {
            Log.d(TAG, "Stopping current recording")
            curRecording.stop()
            recording = null
            return
        }

        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(this, mediaStoreOutputOptions)
            .apply {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.RECORD_AUDIO
                    ) ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    withAudioEnabled()
                    Log.d(TAG, "Audio recording enabled")
                } else {
                    Log.d(TAG, "Audio recording permission not granted")
                }
            }
            .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
                when (recordEvent) {
                    is VideoRecordEvent.Start -> {
                        Log.d(TAG, "Video recording started")
                        captureButton.apply {
                            text = "Stop Capture"
                            isEnabled = true
                        }
                    }

                    is VideoRecordEvent.Finalize -> {
                        if (!recordEvent.hasError()) {
                            val msg =
                                "Video capture succeeded: ${recordEvent.outputResults.outputUri}"
                            Log.d(TAG, msg)
                            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                            calculateHeartRate(recordEvent.outputResults.outputUri)
                        } else {
                            recording?.close()
                            recording = null
                            Log.e(TAG, "Video capture failed: ${recordEvent.error}")
                            Toast.makeText(
                                baseContext,
                                "Video capture failed: ${recordEvent.error}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        captureButton.apply {
                            text = "Start Capture"
                            isEnabled = true
                        }
                    }
                }
            }
    }

    private fun calculateHeartRate(uri: Uri) {
        Log.d(TAG, "Calculating heart rate from video: $uri")
        lifecycleScope.launch {
            try {
                heartRate = HeartRateCalculator.calculateHeartRate(uri, contentResolver)
                Log.d(TAG, "Heart rate calculated: $heartRate BPM")
                resultTextView.text = "Heart Rate: $heartRate BPM"
            } catch (e: Exception) {
                Log.e(TAG, "Error calculating heart rate", e)
                resultTextView.text = "Error: ${e.message}"
            }
        }
    }

    private fun requestPermissions() {
        Log.d(TAG, "Requesting permissions: ${REQUIRED_PERMISSIONS.joinToString()}")
        requestPermissionLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy called")
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
            }.toTypedArray()
    }
}