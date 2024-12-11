package com.example.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fragments.CurrentAddressFragment
import com.example.fragments.LeaveAvailableFragment
import com.example.fragments.LeaveHistoryFragment
import com.example.fragments.PermanentAddressFragment

class LeaveinfoTablayoutAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> LeaveAvailableFragment()
            1 -> LeaveHistoryFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}