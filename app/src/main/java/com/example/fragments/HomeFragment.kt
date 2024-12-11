package com.example.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.hrms_xyug.R

class HomeFragment : Fragment() {
    private lateinit var leaveLL: LinearLayout
    private lateinit var llrequestcredit: LinearLayout
    private lateinit var clockinattendance: LinearLayout
    private var backPressedTime: Long = 0
    private var backToast: Toast? = null
    private lateinit var view: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = inflater.inflate(R.layout.fragment_home, container, false)
        init()
        handleBackPressed()

        return view
    }

    private fun init() {

    }

    private fun handleBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    backToast?.cancel()
                    requireActivity().finishAffinity()
                } else {
                    backToast = Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT)
                    backToast?.show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }
}
