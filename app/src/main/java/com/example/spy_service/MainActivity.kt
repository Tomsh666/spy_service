package com.example.spy_service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text: TextView = findViewById(R.id.textView)
        val stop_button:Button = findViewById(R.id.stop_button )
        val start_button:Button = findViewById(R.id.start_button)
        var n : Boolean = false
        start_button.setOnClickListener(){
            if(!n){
                text.text = "Service is running"
                n = true
            }
            else
            {
                text.text = "Service is already running"
            }
        }
        stop_button.setOnClickListener(){
            if(n){
                text.text = "Service is not running"
                n = false
            }
            else
            {
                text.text = "Service is not running yet"
            }
        }
    }
}