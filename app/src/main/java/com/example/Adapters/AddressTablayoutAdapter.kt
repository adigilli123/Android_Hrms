package com.example.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fragments.CurrentAddressFragment
import com.example.fragments.PermanentAddressFragment

class AddressTablayoutAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CurrentAddressFragment()
            1 -> PermanentAddressFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}