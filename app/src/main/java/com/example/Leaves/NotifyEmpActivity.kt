package com.example.Leaves

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.AttendanceHistoryAdapter
import com.example.Adapters.NotifiyempAdapter
import com.example.Interface.AttendanceHistoryModel
import com.example.Interface.NotifyempModel
import com.example.Interface.ServerApisInterface
import com.example.hrms_xyug.R
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotifyEmpActivity : AppCompatActivity() {

    private lateinit var notifyemp_back:ImageView
    private lateinit var notifyempRecyclerview:RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_emp)

        notifyemp_back=findViewById(R.id.notifyemp_back)
        notifyemp_back.setOnClickListener {
            onBackPressed()
        }

        notifyempRecyclerview=findViewById(R.id.notifyempRecyclerview)
        getNotifyEmployee()
    }

    private fun getNotifyEmployee() {
        val token = AccountUtils.getAccessToken(applicationContext)
        val apiService: ServerApisInterface =
            RetrofitClient.get().create(ServerApisInterface::class.java)
        Log.d("Token", "Token: $token")

        val call: Call<NotifyempModel> = apiService.getNotifyEmp(token)
        call.enqueue(object : Callback<NotifyempModel> {
            override fun onResponse(
                call: Call<NotifyempModel>, response: Response<NotifyempModel>
            ) {
                when (response.code()) {
                    200 -> {
                        // Success: Process the response
                        val notifyempmodel = response.body()
                        notifyempRecyclerview.apply {
                            layoutManager = LinearLayoutManager(applicationContext)
                            adapter = NotifiyempAdapter(notifyempmodel?.data ?: arrayListOf())
                        }
                    }

                    400 -> {
                        Toast.makeText(this@NotifyEmpActivity, "Bad Request: Please check your inputs", Toast.LENGTH_SHORT).show()
                    }
                    404 -> {
                        Toast.makeText(this@NotifyEmpActivity, "Not Found: The requested data does not exist", Toast.LENGTH_SHORT).show()
                    }

                    500 -> {
                        Toast.makeText(this@NotifyEmpActivity, "Server Error: Please try again later", Toast.LENGTH_SHORT).show()
                    }

                    else -> {
                        Toast.makeText(this@NotifyEmpActivity, "Unexpected Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<NotifyempModel>, t: Throwable) {
                // Failure: Handle connection issues or other exceptions
                Log.e("NotifyEmployee", "Failed to fetch notify employee data: ${t.message}")
                Toast.makeText(this@NotifyEmpActivity, "Connection Error: Unable to reach the server", Toast.LENGTH_SHORT).show()
            }
        })
    }

}