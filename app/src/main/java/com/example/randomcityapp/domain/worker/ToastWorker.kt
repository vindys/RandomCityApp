package com.example.randomcityapp.domain.worker

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters

class ToastWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val cityName = inputData.getString("cityName") ?: return Result.failure()

        // Toast must be shown on main thread
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, "You selected $cityName", Toast.LENGTH_SHORT).show()
        }

        return Result.success()
    }
}
