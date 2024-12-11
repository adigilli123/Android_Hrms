package com.example.hrms_xyug

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.EducationAdapter
import com.example.Adapters.ExperienceAdapter
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EducationDetailsActivity : AppCompatActivity() {
    private lateinit var eduaction_back: ImageView
    private lateinit var education_recyclerview: RecyclerView
    private lateinit var eduactionAdapter: EducationAdapter
    private lateinit var token: String
    private lateinit var education_Tv: TextView
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_education_details)

        education_Tv=findViewById(R.id.education_Tv)
        education_recyclerview=findViewById(R.id.education_recyclerview)
        eduaction_back = findViewById(R.id.eduaction_back)

        eduaction_back.setOnClickListener {
            onBackPressed()
        }

        education_Tv.setOnClickListener {
            val intent = Intent(this, AddEducationActivity::class.java)
            startActivity(intent)
        }

        education_recyclerview.layoutManager = LinearLayoutManager(this)

       getEducationData()
    }

    private fun getEducationData() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawToken", token)
            showRefreshDialogue()

            val getEmployeeList = apiService.getSingleEmployee(token)
            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
                override fun onResponse(
                    call: Call<SingleEmployeeModel>,
                    response: Response<SingleEmployeeModel>
                ) {
                   hideRefreshDialog()
                    val statusCode = response.code()
                    Log.e("ResponseStatus", "$statusCode")

                    if (statusCode == 200 || statusCode == 201) {
                        response.body()?.let { singleEmployeeModel ->
                            singleEmployeeModel.data?.expdata?.education?.let { educationList ->
                                if (educationList.isNotEmpty()) {
                                    val educationAdapter = EducationAdapter(educationList)
                                    education_recyclerview.adapter = educationAdapter
                                } else {
                                    Log.e("EducationList", "No education data available")
                                }
                            } ?: run {
                                Log.e("Expdata", "No education data found")
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
        val intent = Intent(this, PersonalDetails::class.java)
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
