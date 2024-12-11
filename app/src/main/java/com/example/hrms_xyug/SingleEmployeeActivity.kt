package com.example.hrms_xyug

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.AllEmpAdapter
import com.example.Interface.AllEmployeeModel
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmpModel
import com.example.Interface.SingleEmployeeModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class SingleEmployeeActivity : AppCompatActivity() {
    private lateinit var singleemp_back:ImageView
    private lateinit var singleEmpImage:ImageView
    private lateinit var mail_singleemp:ImageView
    private lateinit var message_singleemp:ImageView
    private lateinit var managerpicture:ImageView
    private lateinit var singleemp_name:TextView
    private lateinit var managerDesignation:TextView
    private lateinit var managername:TextView
    private lateinit var singleemp_designation:TextView
    private lateinit var professionalsummarydeatisl:TextView
    private lateinit var token:String
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private lateinit var employeeid:String
    private lateinit var email:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_employee)

        employeeid = intent.getStringExtra("employee_number") ?: ""

        singleEmpImage=findViewById(R.id.singleEmpImage)

        managerpicture=findViewById(R.id.managerpicture)
        message_singleemp=findViewById(R.id.message_singleemp)
        message_singleemp.setOnClickListener {
            val url = "https://mail.google.com/chat/u/0/#chat/space/AAAAioO6IC4"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }


        mail_singleemp=findViewById(R.id.mail_singleemp)

        professionalsummarydeatisl=findViewById(R.id.professionalsummarydeatisl)
        singleemp_name=findViewById(R.id.singleemp_name)
        managerDesignation=findViewById(R.id.managerDesignation)
        managername=findViewById(R.id.managername)
        singleemp_designation=findViewById(R.id.singleemp_designation)
        singleemp_back=findViewById(R.id.singleemp_back)
        singleemp_back.setOnClickListener {
            val intent = Intent(this,AllEmployeesActivity::class.java)
            startActivity(intent)
        }
        getSingleEmp()
    }

    private fun getSingleEmp() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.d("Tokencvxvxvx", "Token: $token")
         showRefreshDialogue()

            val getEmployeeList = apiService.getEmployeee(token,employeeid)
            getEmployeeList.enqueue(object : Callback<SingleEmpModel> {
                override fun onResponse(
                    call: Call<SingleEmpModel>,
                    response: Response<SingleEmpModel>) {
                    val statusCode = response.code()
                    Log.d("ResponseCode", "$statusCode")
                 hideRefreshDialog()
                    if (response.isSuccessful) {
                        val singleEmpModel = response.body()

                        singleEmpModel?.let {

                            Log.d("SingleEmployeeDetails", "Employee Name: ${it.firstName}")
                            singleemp_name.text = it.firstName?: "No Summary"

                            Log.d("SingleEmployeeDetails", "Employee Designation: ${it.job}")
                            singleemp_designation.text = it.job?: "No Summary"

                            Log.d("SingleEmployeeDetails", "Manager Name: ${it.reportingTo}")
                            managername.text = it.reportingTo?: "No Summary"

                            Log.d("SingleEmployeeDetails", "Manager Designation: ${it.reporterjob}")
                            managerDesignation.text = it.reporterjob?: "No Summary"

                            Log.d("SingleEmployeeDetails", "Professional Summary: ${it.proffessionalsummary}")
                            professionalsummarydeatisl.text = it.proffessionalsummary ?: "No Summary"


                            if (!it.reporterimage.isNullOrEmpty()) {
                                Picasso.get()
                                    .load(it.reporterimage)
                                    .into(managerpicture)
                            } else {
                                managerpicture.setImageResource(R.drawable.ic_profile)
                            }


                            if (it.photo.isNullOrEmpty()) {
                                singleEmpImage.setImageResource(R.drawable.ic_profile)
                                Log.d("SingleEmployeeActivity", "Photo URL is null or empty. Setting default image.")
                            } else {
                                try {

                                    if (it.photo!!.trim().isNotEmpty()) {
                                        Picasso.get()
                                            .load(it.photo)
                                            .into(singleEmpImage)
                                        Log.d("SingleEmployeeActivity", "Loading image from URL: ${it.photo}")
                                    } else {

                                        singleEmpImage.setImageResource(R.drawable.ic_profile)
                                        Log.d("SingleEmployeeActivity", "Photo URL is empty after trimming. Setting default image.")
                                    }
                                } catch (e: Exception) {

                                    Log.e("SingleEmployeeActivity", "Failed to load image from URL: ${it.photo}", e)
                                    singleEmpImage.setImageResource(R.drawable.ic_profile)
                                }
                            }

                            mail_singleemp.setOnClickListener {
                                singleEmpModel?.email?.let { email ->
                                    if (email.isNotEmpty()) {
                                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                                            data = Uri.parse("mailto:$email")
                                        }
                                        try {
                                            startActivity(intent)
                                        } catch (e: ActivityNotFoundException) {
                                            Log.e("SingleEmployeeActivity", "No email client found to handle the intent.", e)
                                            Toast.makeText(this@SingleEmployeeActivity, "No email app found.", Toast.LENGTH_SHORT).show()
                                        } catch (e: Exception) {
                                            Log.e("SingleEmployeeActivity", "Error occurred while trying to send email.", e)
                                            Toast.makeText(this@SingleEmployeeActivity, "Error occurred while trying to send email.", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Log.e("SingleEmployeeActivity", "Email address is empty.")
                                        Toast.makeText(this@SingleEmployeeActivity, "Email address is empty.", Toast.LENGTH_SHORT).show()
                                    }
                                } ?: run {
                                    Log.e("SingleEmployeeActivity", "Email address is not available.")
                                    Toast.makeText(this@SingleEmployeeActivity, "Email address is not available.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        Log.e("ErrorResponse", response.errorBody()?.string() ?: "No error body")
                    }
                }

                override fun onFailure(call: Call<SingleEmpModel>, t: Throwable) {
                 hideRefreshDialog()
                    Log.e("onFailure", t.message ?: "Unknown error")
                }
            })
        } catch (e: Exception) {
         hideRefreshDialog()
            Log.e("Exception", e.message ?: "Unknown error")
        }
    }

    override fun onBackPressed() {
       val intent = Intent(this,AllEmployeesActivity::class.java)
        startActivity(intent)
    }

    private fun showRefreshDialogue() {
        dialog1 = Dialog(this)
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog1.setContentView(R.layout.activity_animate)
        dialog1.setCanceledOnTouchOutside(false)
        dialog1.setCancelable(false)
        dialog1.show()
        dialog1.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val textView: TextView = dialog1.findViewById(R.id.connecting)
        imageView = dialog1.findViewById(R.id.image_rottate)
        val lp = dialog1.window?.attributes
        lp?.dimAmount = 0.7f
        dialog1.window?.attributes = lp
        handler.postDelayed(updateTimerThread, 100)
    }

    private val updateTimerThread: Runnable = object : Runnable {
        override fun run() {
            imageView.visibility = View.VISIBLE
            handler.postDelayed(this, 1000)
            val rotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotate.duration = 1000
            rotate.interpolator = LinearInterpolator()
            imageView.startAnimation(rotate)
        }
    }

    private fun hideRefreshDialog() {
        handler.removeCallbacks(updateTimerThread)
        dialog1.dismiss()
    }
}