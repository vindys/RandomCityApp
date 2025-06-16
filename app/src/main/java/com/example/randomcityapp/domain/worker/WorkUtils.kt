package com.example.randomcityapp.domain.worker

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit

fun scheduleToastForCity(context: Context, cityName: String) {
    val data = workDataOf("cityName" to cityName)

    val request = OneTimeWorkRequestBuilder<ToastWorker>()
        .setInitialDelay(5, TimeUnit.SECONDS)
        .setInputData(data)
        .build()

    WorkManager.getInstance(context).enqueue(request)
}