package com.example.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import com.example.hrms_xyug.HomeActivityViewController
import com.example.hrms_xyug.PeersMyTeamActivity
import com.example.hrms_xyug.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MyTeamFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var peersProfRelative:RelativeLayout
    private lateinit var more_Tv:TextView
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


        val  view = inflater.inflate(R.layout.fragment_my_team, container, false)

        peersProfRelative=view.findViewById(R.id.peersProfRelative)
        more_Tv=view.findViewById(R.id.more_Tv)

        peersProfRelative.setOnClickListener {

            val intent=Intent(activity,PeersMyTeamActivity::class.java)
            startActivity(intent)
        }

        more_Tv.setOnClickListener {
            val intent=Intent(activity, PeersMyTeamActivity::class.java)
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

        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyTeamFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}