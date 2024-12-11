package com.example.fragments

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.CACHE.CacheManager
import com.example.Interface.ProfileModel
import com.example.Interface.ServerApisInterface
import com.example.Leaves.ApplyLeave
import com.example.Leaves.LeaveInfoActivity
import com.example.hrms_xyug.HomeActivityViewController
import com.example.hrms_xyug.MainActivity
import com.example.hrms_xyug.ManagerApproveRejectActivity
import com.example.hrms_xyug.PersonalDetails
import com.example.hrms_xyug.R
import com.xyug.hrms.AccountUtils
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ChatFragment : Fragment() {

    private val idToPass = "general_id"
    private lateinit var applyleave_Relative:RelativeLayout
    private lateinit var leaveinfo_Relative:RelativeLayout
    private lateinit var payslip_Relative:RelativeLayout
    private lateinit var managerapprove_Rl:RelativeLayout
    private lateinit var applyleave_arrow:TextView
    private lateinit var leaveinfo_arrow:TextView
    private lateinit var payslip_arrow:TextView
    private lateinit var manager_arrow:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)
        init(view)

        applyleave_Relative=view.findViewById(R.id.applyleave_Relative)
        applyleave_Relative.setOnClickListener {
            val intent = Intent(activity,ApplyLeave::class.java)
            startActivity(intent)
        }
        leaveinfo_Relative=view.findViewById(R.id.leaveinfo_Relative)
        leaveinfo_Relative.setOnClickListener {
            val intent = Intent(activity, LeaveInfoActivity::class.java)
            startActivity(intent)
        }
        payslip_Relative=view.findViewById(R.id.payslip_Relative)
        applyleave_arrow=view.findViewById(R.id.applyleave_arrow)
        applyleave_arrow.setOnClickListener {
            val intent = Intent(activity,ApplyLeave::class.java)
            startActivity(intent)
        }
        managerapprove_Rl=view.findViewById(R.id.managerapprove_Rl)
        managerapprove_Rl.setOnClickListener {
            val intent = Intent(activity, ManagerApproveRejectActivity::class.java)
            startActivity(intent)
        }
        manager_arrow=view.findViewById(R.id.manager_arrow)
        manager_arrow.setOnClickListener {
            val intent = Intent(activity,ManagerApproveRejectActivity::class.java)
            startActivity(intent)
        }
        leaveinfo_arrow=view.findViewById(R.id.leaveinfo_arrow)
        payslip_arrow=view.findViewById(R.id.payslip_arrow)
        return view
    }

    private fun init(view: View) {

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
    }


}
