package com.example.Adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fragments.ChildrenFragment
import com.example.fragments.CurrentAddressFragment
import com.example.fragments.ParentsFragment
import com.example.fragments.PermanentAddressFragment
import com.example.fragments.SiblingsFragment

class RelationTablayoutAdapter (activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ChildrenFragment()
            1 -> ParentsFragment()
            2 -> SiblingsFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}