package com.example.spy_service

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text: TextView = findViewById(R.id.textView)
        val stop_button:Button = findViewById(R.id.stop_button )
        val start_button:Button = findViewById(R.id.start_button)
        requestPermissions()
        start_button.setOnClickListener(){
            startTask()
            text.text = getString(R.string.running)
        }
        stop_button.setOnClickListener(){
            stopTask()
            text.text = getString(R.string.not_running)
        }
    }

    private var currentPermission: String = ""

    private val permissionsToRequest = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.READ_SMS,
        Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
    )

    private val requestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                if (!shouldShowRequestPermissionRationale(currentPermission)) {
                    Log.d("Tag","$currentPermission granted")
                    showPermissionRequiredDialog()
                }
            }
        }

    private fun requestPermissions() {
        for (permission in permissionsToRequest) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                currentPermission = permission
                requestPermissionLauncher.launch(permission)
            }
            Log.d("Tag","$permission granted")
        }
    }


    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Grant access to perform the task")
            .setPositiveButton("Settings") { _, _ -> openAppSettings() }
            .setCancelable(false)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }

//    private fun startTask() {
//        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
//        val task = PeriodicWorkRequestBuilder<BackgroundSpyWorker>(
//            15, TimeUnit.MINUTES,
//            5, TimeUnit.MINUTES)
//            .addTag("SPYJOB")
//            .setConstraints(constrains)
//            .build()
//            WorkManager.getInstance()
//            .enqueueUniquePeriodicWork("SPYJOB", ExistingPeriodicWorkPolicy.KEEP, task)
//    }

    private fun startTask() {
        val constrains = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val task = OneTimeWorkRequestBuilder<BackgroundSpyWorker>()
            .addTag("SPYJOB")
            .setConstraints(constrains)
            .build()

        WorkManager.getInstance().enqueue(task)
    }

    private fun stopTask() = WorkManager.getInstance().cancelAllWorkByTag("SPYJOB")
}