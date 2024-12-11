package com.example.hrms_xyug

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.fragments.OrganizationFragment

class BirthdayAcitivity : AppCompatActivity() {
    private lateinit var upcomingbirthdays_back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthday_acitivity)

        upcomingbirthdays_back=findViewById(R.id.upcomingbirthdays_back)
        upcomingbirthdays_back.setOnClickListener {

            navigateToProfileFragment()
        }
    }

    override fun onBackPressed() {
        navigateToProfileFragment()
    }


    private fun navigateToProfileFragment() {
        val intent = Intent(this, HomeActivityViewController::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish() // Finish the current activity
    }

}