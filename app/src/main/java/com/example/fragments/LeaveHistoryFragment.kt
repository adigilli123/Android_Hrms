package com.example.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.LeaveHistoryAdapter
import com.example.Interface.LeaveHistoryModel
import com.example.Interface.LeavetypeModel
import com.example.Interface.ServerApisInterface
import com.example.hrms_xyug.R
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class LeaveHistoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var leavehistoryrecyclerview:RecyclerView

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
        val view:View= inflater.inflate(R.layout.fragment_leave_history, container, false )


        leavehistoryrecyclerview=view.findViewById(R.id.leavehistoryrecyclerview)
        getLeavetype1()

        return view
    }

    private fun getLeavetype1() {
        val token = AccountUtils.getAccessToken(requireContext())
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        Log.d("Token", "Token: $token")

        val call: Call<LeaveHistoryModel> = apiService.getLeaveHistory(token)
        call.enqueue(object : Callback<LeaveHistoryModel> {
            override fun onResponse(call: Call<LeaveHistoryModel>, response: Response<LeaveHistoryModel>) {
                if (response.isSuccessful) {
                    val leaveHistory = response.body()?.data
                    if (leaveHistory != null) {

                        leavehistoryrecyclerview.apply {
                            layoutManager = LinearLayoutManager(context)
                            adapter = LeaveHistoryAdapter(listOf(leaveHistory))
                        }
                    } else {
                        Toast.makeText(context, "No leave history available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to fetch leave history", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LeaveHistoryModel>, t: Throwable) {
                Log.e("Leavetype", "Failed to fetch leave types: ${t.message}")
                Toast.makeText(context, "Connection Error: Unable to reach the server", Toast.LENGTH_SHORT).show()
            }
        })
    }


}