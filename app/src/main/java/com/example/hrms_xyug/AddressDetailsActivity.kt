package com.example.hrms_xyug

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.example.Adapters.AddressTablayoutAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AddressDetailsActivity : AppCompatActivity() {

    private lateinit var addressdetails_back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_details)

        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        addressdetails_back=findViewById(R.id.addressdetails_back)

        addressdetails_back.setOnClickListener {

            val intent = Intent(this,PersonalDetails::class.java)
            startActivity(intent)
        }

        val adapter = AddressTablayoutAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Current Address"
                1 -> "Permanent Address"
                else -> null
            }
        }.attach()
    }
}