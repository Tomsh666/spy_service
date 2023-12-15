package com.example.spy_service

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class BackgroundSpyWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    private val TAG = "SPY_TAG"
    /* в этом переопределённом методе должен быть весь функционал работы
    он запускает в отдельном background потоке
    метод всегда должен возвращать какой-либо результат работы */
    override fun doWork(): Result {
        return try {
            sendInformation()
            Log.d(TAG, "success work")
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "exception occurred: ", e)
            Result.failure()
        }
    }

    private fun getAvailableMemory(): Int {
        return 0
    }

    private fun sendInformation() {
        Log.d(TAG, "")
    }

}