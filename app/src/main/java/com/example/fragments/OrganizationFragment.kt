package com.example.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import com.example.hrms_xyug.AllEmployeesActivity
import com.example.hrms_xyug.AnniversarytAcitivity
import com.example.hrms_xyug.AnnouncementAcitivity
import com.example.hrms_xyug.BirthdayAcitivity
import com.example.hrms_xyug.HomeActivityViewController
import com.example.hrms_xyug.NewemployeeAcitivity
import com.example.hrms_xyug.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class OrganizationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val idToPass = "general_id1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_organization, container, false)

        val empDirectorProfRelative = view.findViewById<View>(R.id.empDirectorProfRelative)
        val announsmentProfRelative = view.findViewById<View>(R.id.announsmentProfRelative)
        val birthdayProfRelative = view.findViewById<View>(R.id.birthdayProfRelative)
        val anniversaryProfRelative = view.findViewById<View>(R.id.anniversaryProfRelative)
        val newempProfRelative = view.findViewById<View>(R.id.newempProfRelative)

        empDirectorProfRelative.setOnClickListener {
            val intent = Intent(activity, AllEmployeesActivity::class.java)
            startActivity(intent)
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Log.d("ChatFragment", "Back button pressed, navigating to HomeFragment")

                // Pass ID to HomeActivityViewController
                (activity as? HomeActivityViewController)?.apply {
                    updateBottomNavigationBasedOnId(idToPass)
                }

                // Navigate to HomeFragment
                parentFragmentManager.beginTransaction()
                    .replace(R.id.flFragment, HomeFragment(), "HomeFragmentTag")
                    .commit()
                parentFragmentManager.executePendingTransactions()
            }
        })

//        requireActivity().onBackPressedDispatcher.addCallback(context, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                Log.d("OrganizationFragment", "Back button pressed, navigating to HomeFragment")
//
//                (activity as? HomeActivityViewController)?.apply {
//                    updateBottomNavigationBasedOnId(idToPass)
//                }
//
//                parentFragmentManager.beginTransaction()
//                    .replace(R.id.flFragment, HomeFragment(), "HomeFragmentTag")
//                    .commit()
//                parentFragmentManager.executePendingTransactions()
//            }
//        })

        announsmentProfRelative.setOnClickListener {
            val intent = Intent(activity, AnnouncementAcitivity::class.java)
            startActivity(intent)
        }

        birthdayProfRelative.setOnClickListener {
            val intent = Intent(activity, BirthdayAcitivity::class.java)
            startActivity(intent)
        }

        anniversaryProfRelative.setOnClickListener {
            val intent = Intent(activity, AnniversarytAcitivity::class.java)
            startActivity(intent)
        }

        newempProfRelative.setOnClickListener {
            val intent = Intent(activity, NewemployeeAcitivity::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OrganizationFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}