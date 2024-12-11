package com.example.hrms_xyug

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.viewpager2.widget.ViewPager2
import com.example.Adapters.AddressTablayoutAdapter
import com.example.Adapters.RelationTablayoutAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RelationDetailsActivity : AppCompatActivity() {
    private lateinit var relationdetails_back:ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relation_details)

        val tabLayout = findViewById<TabLayout>(R.id.relationtabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.relationviewPager)


        relationdetails_back=findViewById(R.id.relationdetails_back)
        relationdetails_back.setOnClickListener {

            val intent = Intent(this,PersonalDetails::class.java)
            startActivity(intent)
        }


        // Set up the adapter for ViewPager2
        val adapter = RelationTablayoutAdapter(this)
        viewPager.adapter = adapter

        // Connect TabLayout and ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Children"
                1 -> "Parents"
                2 -> "Siblings"
                else -> null
            }
        }.attach()
    }
}