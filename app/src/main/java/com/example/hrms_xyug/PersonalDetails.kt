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
import android.widget.RelativeLayout
import android.widget.TextView
import android.window.OnBackInvokedDispatcher
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.Interface.BussinessUnitModel
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.example.Interface.UploadImageModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalDetails : AppCompatActivity() {

    private lateinit var primaryProfRelative:RelativeLayout
    private lateinit var contactProfRelative:RelativeLayout
    private lateinit var addressProfRelative:RelativeLayout
    private lateinit var relationProfRelative:RelativeLayout
    private lateinit var experienceProfRelative:RelativeLayout
    private lateinit var educationProfRelative:RelativeLayout
    private lateinit var summaryProfRelative:RelativeLayout
    private lateinit var personaldetails_back:ImageView
    private lateinit var imageView_Iv:ImageView
    private lateinit var token:String
    private lateinit var name:TextView
    private lateinit var location:TextView
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog

    override fun onResume() {
        super.onResume()
        getDetails()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details)

        name=findViewById(R.id.name)
        location=findViewById(R.id.location)
        personaldetails_back=findViewById(R.id.personaldetails_back)
        personaldetails_back.setOnClickListener {
            navigateToProfileFragment()
        }
        val imageUrl = AccountUtils.getImageUrl(this)
        imageView_Iv=findViewById(R.id.imageView_Iv)

        getDetails()
        getBusinnessDetails()

        primaryProfRelative=findViewById(R.id.primaryProfRelative)
        primaryProfRelative.setOnClickListener {

            val intent = Intent(this, PersonalDetailsActivity::class.java)
            startActivity(intent)
        }

        contactProfRelative=findViewById(R.id.contactProfRelative)
        contactProfRelative.setOnClickListener {

            val intent = Intent(this, ContactDetailsActivity::class.java)
            startActivity(intent)
        }

        addressProfRelative=findViewById(R.id.addressProfRelative)
        addressProfRelative.setOnClickListener {

            val intent = Intent(this, AddressDetailsActivity::class.java)
            startActivity(intent)
        }

        relationProfRelative=findViewById(R.id.relationProfRelative)
        relationProfRelative.setOnClickListener {

            val intent = Intent(this, RelationDetailsActivity::class.java)
            startActivity(intent)
        }

        experienceProfRelative=findViewById(R.id.experienceProfRelative)
        experienceProfRelative.setOnClickListener {

            val intent = Intent(this, ExperienceDetailsActivity::class.java)
            startActivity(intent)
        }

        educationProfRelative=findViewById(R.id.educationProfRelative)
        educationProfRelative.setOnClickListener {

            val intent = Intent(this, EducationDetailsActivity::class.java)
            startActivity(intent)
        }

        summaryProfRelative=findViewById(R.id.summaryProfRelative)
        summaryProfRelative.setOnClickListener {

            val intent = Intent(this, ProfessionalSummaryDetailsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getBusinnessDetails(){
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        val token = AccountUtils.getAccessToken(applicationContext)

        apiService.getBussinessUnit(token).enqueue(object : Callback<BussinessUnitModel> {
            override fun onResponse(call: Call<BussinessUnitModel>, response: Response<BussinessUnitModel>) {

                if (response.isSuccessful) {
                    val bussinessUnitModel = response.body()

                    // Assuming `org`, `itfarm`, and `location` are TextViews
                    bussinessUnitModel?.let {
                        location.text = it.busslocation ?: "N/A"
                    }
                } else {

                }
            }
            override fun onFailure(call: Call<BussinessUnitModel>, t: Throwable) {

            }
        })
    }

    private fun getDetails() {
       // showRefreshDialogue()
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        token=AccountUtils.getAccessToken(applicationContext)
        val call = apiService.getSingleEmployee(token)
        call.enqueue(object : Callback<SingleEmployeeModel> {
            override fun onResponse(call: Call<SingleEmployeeModel>, response: Response<SingleEmployeeModel>) {
                val statusCode = response.code()

                Log.e("detailsStatusCode", "Status Code: $statusCode")
                hideRefreshDialog()
                if (response.isSuccessful) {
                    val employeeModel = response.body()
                    employeeModel?.data?.employeedata?.let { employeeData ->
                        val firstName = employeeData.firstName
                        val lastName = employeeData.lastName

                        val fullName = "$firstName $lastName"
                        name.text = fullName
                        val photoUrl = employeeData.photo
                        if (!photoUrl.isNullOrEmpty()) {
                            Picasso.get()
                                .load(photoUrl)
                                .into(imageView_Iv)
                            Log.i("EmployeeDetails", "Loading photo from URL: $photoUrl")
                        } else {
                            imageView_Iv.setImageResource(R.drawable.ic_profile)
                            Log.w("EmployeeDetails", "Photo URL is null or empty. Setting default image.")
                        }
                        Log.i("EmployeeDetails", "Employee Name: $fullName")
                    }
                } else {
                    Log.e("error", "Unexpected error: $statusCode - ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<SingleEmployeeModel>, t: Throwable) {
                hideRefreshDialog()
                Log.e("failure", "Upload failed: ${t.message}")
            }
        })
    }

    override fun onBackPressed() {
        navigateToProfileFragment()
    }

    private fun navigateToProfileFragment() {
        val intent = Intent(this, HomeActivityViewController::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
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
        if (::dialog1.isInitialized) {
            dialog1.dismiss()
        }
    }
}