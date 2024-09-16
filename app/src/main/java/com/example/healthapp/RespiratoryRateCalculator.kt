package com.example.healthapp

import android.util.Log
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

object RespiratoryRateCalculator {
    private const val TAG = "RespiratoryRateCalc"

    fun calculate(
        accelValuesX: MutableList<Float>,
        accelValuesY: MutableList<Float>,
        accelValuesZ: MutableList<Float>
    ): Int {
        Log.d(TAG, "Starting respiratory rate calculation")

        var previousValue = 10f
        var k = 0
        for (i in 11 until accelValuesY.size) {
            val currentValue = sqrt(
                accelValuesZ[i].toDouble().pow(2.0) +
                        accelValuesX[i].toDouble().pow(2.0) +
                        accelValuesY[i].toDouble().pow(2.0)
            ).toFloat()

            if (abs(previousValue - currentValue) > 0.15) {
                k++
            }
            previousValue = currentValue
        }

        val ret = (k.toDouble() / 45.00)
        val result = (ret * 30).toInt()

        Log.d(TAG, "Calculation completed. K=$k, Intermediate result=$ret")
        Log.i(TAG, "Final respiratory rate: $result breaths per minute")

        return result
    }
}