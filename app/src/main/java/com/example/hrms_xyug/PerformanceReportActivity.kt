package com.example.hrms_xyug

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class PerformanceReportActivity : AppCompatActivity() {

    private lateinit var Performance_back: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_report)

        Performance_back=findViewById(R.id.Performance_back)

        Performance_back.setOnClickListener {
            // Permission already granted, proceed with initial actions
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(applicationContext, HomeActivityViewController::class.java))
    }
}