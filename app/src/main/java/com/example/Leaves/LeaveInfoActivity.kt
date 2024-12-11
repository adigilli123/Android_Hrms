package com.example.Leaves

import android.content.Intent
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.Adapters.AddressTablayoutAdapter
import com.example.Adapters.LeaveinfoTablayoutAdapter
import com.example.Interface.LeavetypeModel
import com.example.Interface.ServerApisInterface
import com.example.hrms_xyug.PersonalDetails
import com.example.hrms_xyug.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaveInfoActivity : AppCompatActivity() {

    private lateinit var leaveinfo_back: ImageView
    private lateinit var tabLayout_leaveinfo: TabLayout
    private lateinit var viewPager_leaveinfo: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leave_info)

        tabLayout_leaveinfo = findViewById(R.id.tabLayout_leaveinfo)
        viewPager_leaveinfo = findViewById(R.id.viewPager_leaveinfo)
        leaveinfo_back=findViewById(R.id.leaveinfo_back)

        leaveinfo_back.setOnClickListener {
           onBackPressed()
        }

        val adapter = LeaveinfoTablayoutAdapter(this)
        viewPager_leaveinfo.adapter = adapter

        TabLayoutMediator(tabLayout_leaveinfo, viewPager_leaveinfo) { tab, position ->
            tab.text = when (position) {
                0 -> "Leaves Available"
                1 -> "Leaves History"
                else -> null
            }
        }.attach()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}