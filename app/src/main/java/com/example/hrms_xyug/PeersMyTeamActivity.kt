package com.example.hrms_xyug

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class PeersMyTeamActivity : AppCompatActivity() {
    private lateinit var peers_back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_peers_my_team)

        peers_back=findViewById(R.id.peers_back)
        peers_back.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        navigateToProfileFragment()
    }

    private fun navigateToProfileFragment() {
        val intent = Intent(this, HomeActivityViewController::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }
}