package com.example.hrms_xyug

import android.app.Dialog
import android.content.Intent
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
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class ContactDetailsActivity : AppCompatActivity() {
    private lateinit var contactdetails_back:ImageView
    private lateinit var contactnumber_Tv:TextView
    private lateinit var emailTv:TextView
    private lateinit var emergencyrelation_Tv:TextView
    private lateinit var emergencyNumberTv:TextView
    private lateinit var token:String
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_details)

        contactnumber_Tv=findViewById(R.id.contactnumber_Tv)
        emergencyrelation_Tv=findViewById(R.id.emergencyrelation_Tv)
        emergencyNumberTv=findViewById(R.id.emergencyNumberTv)
        emailTv=findViewById(R.id.emailTv)
        contactdetails_back=findViewById(R.id.contactdetails_back)
        contactdetails_back.setOnClickListener {
            val intent = Intent(this,PersonalDetails::class.java)
            startActivity(intent)
        }

        getContactDetails()
    }

    private fun getContactDetails() {

        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawTokenregfsdgf",""+token)
            showRefreshDialogue()

            val getEmployeeList = apiService.getSingleEmployee(token)
            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
                override fun onResponse(call: Call<SingleEmployeeModel>, response: Response<SingleEmployeeModel>) {
                    val statusCode = response.code()
                    Log.e("getSingleEmployeeListStatus", "$statusCode")
                    hideRefreshDialog()

                    if (statusCode == 200 || statusCode == 201) {
                        val singleEmployeeModel = response.body()
                        singleEmployeeModel?.let { model ->
                            val employeeData = model.data?.employeedata
                            val jobData = model.data?.jobdata
                            val contact = model.data?.contactdata

                            if (employeeData != null) {
                                Log.e("EmployeeData", "First Name: ${employeeData.firstName}, Last Name: ${employeeData.lastName}, Job Title: ${jobData?.jobTitle}")

                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                contactnumber_Tv.text = contact?.mobilePhone ?: "N/A"
                                emailTv.text = contact?.workEmail ?: "N/A"
//                                emergencyrelation_Tv.text = contact?.employeeNumber?.let { dateFormat.format(it) } ?: "N/A"
//                                emergencyNumberTv.text =  contact?.employeeNumber ?: "N/A"

                            } else {
                                Log.e("Error", "EmployeeData is null")
                            }
                        }
                    } else {
                        Log.e("ErrorResponse", response.errorBody()?.string() ?: "No error body")
                    }
                }

                override fun onFailure(call: Call<SingleEmployeeModel>, t: Throwable) {
                 hideRefreshDialog()

                    Log.e("onFailure", t.toString())
                }
            })
        } catch (e: Exception) {
          hideRefreshDialog()

            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this,PersonalDetails::class.java)
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