package com.example.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.LeaveAvailableAdapter
import com.example.Interface.LeaveavailableModel
import com.example.Interface.LeavetypeModel
import com.example.Interface.ServerApisInterface
import com.example.Leaves.ApplyLeave
import com.example.hrms_xyug.R
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LeaveAvailableFragment : Fragment() {

    private lateinit var leaveavailbledata_Recyclerview: RecyclerView
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
       val view:View= inflater.inflate(R.layout.fragment_leave_available, container, false )

        leaveavailbledata_Recyclerview=view.findViewById(R.id.leaveavailbledata_Recyclerview)

        getLeaveData()

        return view
    }

    private fun getLeaveData() {
        val token = AccountUtils.getAccessToken(requireContext())
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        Log.d("Token", "Token: $token")

        val call: Call<LeaveavailableModel> = apiService.getLeaveavaialbledata(token)
        call.enqueue(object : Callback<LeaveavailableModel> {
            override fun onResponse(call: Call<LeaveavailableModel>, response: Response<LeaveavailableModel>) {
                if (response.isSuccessful) {
                    val leaveAvailableData = response.body()?.data
                    if (leaveAvailableData != null) {
                        val recyclerView = view?.findViewById<RecyclerView>(R.id.leaveavailbledata_Recyclerview)
                        recyclerView?.layoutManager = LinearLayoutManager(context)
                        val adapter = LeaveAvailableAdapter(leaveAvailableData)
                        recyclerView?.adapter = adapter  // Set the adapter
                    } else {
                        Toast.makeText(context, "No leave data available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch leave types", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LeaveavailableModel>, t: Throwable) {
                Log.e("Leavetype", "Failed to fetch leave types: ${t.message}")
                Toast.makeText(context, "Connection Error: Unable to reach the server", Toast.LENGTH_SHORT).show()
            }
        })
    }

}