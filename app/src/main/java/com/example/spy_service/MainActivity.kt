package com.example.spy_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.work.NetworkType
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text: TextView = findViewById(R.id.textView)
        val stop_button:Button = findViewById(R.id.stop_button )
        val start_button:Button = findViewById(R.id.start_button)
        start_button.setOnClickListener(){
            startTask()
            text.text = getString(R.string.running)
        }
        stop_button.setOnClickListener(){
            stopTask()
            text.text = getString(R.string.not_running)
        }
    }
    private fun startTask() {
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val task = PeriodicWorkRequestBuilder<BackgroundSpyWorker>(
            15, TimeUnit.MINUTES,
            5, TimeUnit.MINUTES)
            .addTag("SPYJOB")
            .setConstraints(constrains)
            .build()
        WorkManager.getInstance()
            .enqueueUniquePeriodicWork("spyJob", ExistingPeriodicWorkPolicy.KEEP, task)
    }

    private fun stopTask() = WorkManager.getInstance().cancelAllWorkByTag("SPYJOB")
}