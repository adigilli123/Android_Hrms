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
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.ExperienceAdapter
import com.example.Interface.Experience
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ExperienceDetailsActivity : AppCompatActivity() {
    private lateinit var experiencedetails_back: ImageView
    private lateinit var token: String
    private var imageUrl: String? = null
    private lateinit var experienceAdapter: ExperienceAdapter
    private lateinit var experienceRecycler: RecyclerView
    private lateinit var addexperience_Tv: TextView
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private var experienceList: List<Experience> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experience_details)

        experiencedetails_back = findViewById(R.id.experiencedetails_back)
        addexperience_Tv = findViewById(R.id.addexperience_Tv)
        experienceRecycler = findViewById(R.id.experienceRecycler)

        experiencedetails_back.setOnClickListener {
            val intent = Intent(this, PersonalDetails::class.java)
            startActivity(intent)
        }

        addexperience_Tv.setOnClickListener {
            val intent = Intent(this, AddExperienceActivity::class.java)
            startActivity(intent)
        }

        experienceRecycler.layoutManager = LinearLayoutManager(this)

        getExperinceData()
    }

    private fun getExperinceData() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawToken", token)
            showRefreshDialogue()

            val getEmployeeList = apiService.getSingleEmployee(token)
            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
                override fun onResponse(
                    call: Call<SingleEmployeeModel>, response: Response<SingleEmployeeModel>) {
                    val statusCode = response.code()
                    Log.e("ResponseStatus", "$statusCode")
                 hideRefreshDialog()

                    if (statusCode == 200 || statusCode == 201) {
                        val singleEmployeeModel = response.body()
                        singleEmployeeModel?.let { model -> model.data?.expdata?.experiences?.let { experienceList ->
                                experienceAdapter = ExperienceAdapter(experienceList)
                                experienceRecycler.adapter = experienceAdapter
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
