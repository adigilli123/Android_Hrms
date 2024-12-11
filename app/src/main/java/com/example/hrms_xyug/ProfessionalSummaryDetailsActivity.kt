package com.example.hrms_xyug

import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.Interface.ProfessionalSummaryBodyModel
import com.example.Interface.ProfessionalsummaryModel
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmpModel
import com.example.Interface.SingleEmployeeModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class ProfessionalSummaryDetailsActivity : AppCompatActivity() {
    private lateinit var summarydetails_back: ImageView
    private lateinit var addprofessional_Tv: Button
    private lateinit var editbtn: CardView
    private lateinit var careertext_Tv: TextView
    private lateinit var keyskillstext_Tv: TextView
    private lateinit var summaryqualificationtext_Tv: TextView
    private lateinit var acheivementtext_Tv: TextView
    private lateinit var token: String
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private lateinit var addsummaryqualificationtext_Tv: EditText
    private lateinit var addkeyskillstext_Tv: EditText
    private lateinit var addcareertext_Tv: EditText
    private lateinit var addacheivementtext_Tv: EditText
    private lateinit var addsummaryAdd: Button
    private lateinit var extra_text3: TextView
    private lateinit var summaryerrormessage: TextView
    private lateinit var keyskillserrormessage: TextView
    private lateinit var careererrormessage: TextView
    private lateinit var highlighterrormessage: TextView
    private lateinit var scrollview_prosummary: ScrollView

    override fun onResume() {
        super.onResume()
        getProfessionalSummaryData()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_professional_summary_details)

        scrollview_prosummary=findViewById(R.id.scrollview_prosummary)
        summaryerrormessage=findViewById(R.id.summaryerrormessage)
        keyskillserrormessage=findViewById(R.id.keyskillserrormessage)
        careererrormessage=findViewById(R.id.careererrormessage)
        highlighterrormessage=findViewById(R.id.highlighterrormessage)


        extra_text3=findViewById(R.id.extra_text3)

        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                extra_text3.visibility = View.INVISIBLE
            } else {
                extra_text3.visibility = View.GONE
            }
        }
        addsummaryqualificationtext_Tv = findViewById(R.id.addsummaryqualificationtext_Tv)
        addsummaryqualificationtext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                summaryerrormessage.visibility = View.GONE
                keyskillserrormessage.visibility = View.GONE
                careererrormessage.visibility = View.GONE
                highlighterrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        addkeyskillstext_Tv = findViewById(R.id.addkeyskillstext_Tv)
        addkeyskillstext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                summaryerrormessage.visibility = View.GONE
                keyskillserrormessage.visibility = View.GONE
                careererrormessage.visibility = View.GONE
                highlighterrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addcareertext_Tv = findViewById(R.id.addcareertext_Tv)
        addcareertext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                summaryerrormessage.visibility = View.GONE
                keyskillserrormessage.visibility = View.GONE
                careererrormessage.visibility = View.GONE
                highlighterrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addacheivementtext_Tv = findViewById(R.id.addacheivementtext_Tv)
        addacheivementtext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                summaryerrormessage.visibility = View.GONE
                keyskillserrormessage.visibility = View.GONE
                careererrormessage.visibility = View.GONE
                highlighterrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        careertext_Tv = findViewById(R.id.careertext_Tv)
        keyskillstext_Tv = findViewById(R.id.keyskillstext_Tv)
        summaryqualificationtext_Tv = findViewById(R.id.summaryqualificationtext_Tv)
        acheivementtext_Tv = findViewById(R.id.acheivementtext_Tv)
        addprofessional_Tv = findViewById(R.id.addprofessional_Tv)
        editbtn = findViewById(R.id.editbtn)
        summarydetails_back = findViewById(R.id.summarydetails_back)

        addprofessional_Tv.setOnClickListener {
            careertext_Tv.visibility = View.GONE
            keyskillstext_Tv.visibility = View.GONE
            summaryqualificationtext_Tv.visibility = View.GONE
            acheivementtext_Tv.visibility = View.GONE
            addprofessional_Tv.visibility = View.GONE
            editbtn.visibility = View.GONE
            addsummaryqualificationtext_Tv.visibility=View.VISIBLE
            addkeyskillstext_Tv.visibility=View.VISIBLE
            addcareertext_Tv.visibility=View.VISIBLE
            addacheivementtext_Tv.visibility=View.VISIBLE
            addsummaryAdd.visibility=View.VISIBLE

            setEditTextFromTextView()
        }


        summarydetails_back.setOnClickListener {
            val intent = Intent(this, PersonalDetails::class.java)
            Log.e("intenttttttbutton",""+intent)
            startActivity(intent)
        }

        addsummaryAdd=findViewById(R.id.addsummarybtn)
        addsummaryAdd.setOnClickListener {
            val summaryText = addsummaryqualificationtext_Tv.text.toString().trim()
            val keySkillsText = addkeyskillstext_Tv.text.toString().trim()
            val careerText = addcareertext_Tv.text.toString().trim()
            val achievementText = addacheivementtext_Tv.text.toString().trim()

            summaryerrormessage.text = ""
            summaryerrormessage.visibility = View.GONE
            keyskillserrormessage.text = ""
            keyskillserrormessage.visibility = View.GONE
            careererrormessage.text = ""
            careererrormessage.visibility = View.GONE
            highlighterrormessage.text = ""
            highlighterrormessage.visibility = View.GONE

            if (summaryText.isNotEmpty() && summaryText.length < 3) {
                summaryerrormessage.text = "Please Enter Summary must be at least 3 characters"
                summaryerrormessage.visibility = View.VISIBLE
                scrollview_prosummary.smoothScrollTo(0, summaryerrormessage.top)
                addsummaryqualificationtext_Tv.requestFocus()
                return@setOnClickListener
            }

            if (keySkillsText.isNotEmpty() && keySkillsText.length < 3) {
                keyskillserrormessage.text = "Please Enter Key skills must be at least 3 characters"
                keyskillserrormessage.visibility = View.VISIBLE
                scrollview_prosummary.smoothScrollTo(0, keyskillserrormessage.top)
                addkeyskillstext_Tv.requestFocus()
                return@setOnClickListener
            }

            if (careerText.isNotEmpty() && careerText.length < 3) {
                careererrormessage.text = "Please Enter Career description must be at least 3 characters"
                careererrormessage.visibility = View.VISIBLE
                scrollview_prosummary.smoothScrollTo(0, careererrormessage.top)
                addcareertext_Tv.requestFocus()
                return@setOnClickListener
            }

            if (achievementText.isNotEmpty() && achievementText.length < 3) {
                highlighterrormessage.text = "Please Enter Achievement description must be at least 3 characters"
                highlighterrormessage.visibility = View.VISIBLE
                scrollview_prosummary.smoothScrollTo(0, highlighterrormessage.top)
                addacheivementtext_Tv.requestFocus()
                return@setOnClickListener
            }


            postProfessionalSummaryData()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, PersonalDetails::class.java)
        Log.e("intenttttttbutton",""+intent)
        startActivity(intent)
    }


    private fun getProfessionalSummaryData() {
        showRefreshDialogue()

        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawTokenregfsdgf",""+token)

            val getEmployeeList = apiService.getSingleEmployee(token)
            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
                override fun onResponse(
                    call: Call<SingleEmployeeModel>,
                    response: Response<SingleEmployeeModel>
                ) {
                    val statusCode = response.code()
                    Log.e("getSingleEmployeeListStatus", "$statusCode")
                   hideRefreshDialog()

                    if (statusCode == 200 || statusCode == 201) {
                        val singleEmployeeModel = response.body()
                        singleEmployeeModel?.let { model ->
                            val employeeData = model.data?.employeedata
                            val jobData = model.data?.jobdata
                            val contact = model.data?.contactdata
                            val expData = model.data?.expdata

                            // Handle Employee Data
                            if (employeeData != null) {
                                Log.e("EmployeeData", "First Name: ${employeeData.firstName}, Last Name: ${employeeData.lastName}, Job Title: ${jobData?.jobTitle}")
                                careertext_Tv.text = expData?.professionalSummary?.carrerObjectives
                                keyskillstext_Tv.text = expData?.professionalSummary?.keyskills
                                summaryqualificationtext_Tv.text = expData?.professionalSummary?.qualifications
                                acheivementtext_Tv.text = expData?.professionalSummary?.highlights

                            } else {
                                Log.e("Error", "EmployeeData is null")
                            }
                        }
                    } else {
                        Log.e("ErrorResponse", response.errorBody()?.string() ?: "No error body")
                    }                }

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

    private fun setEditTextFromTextView() {
        addsummaryqualificationtext_Tv.setText(summaryqualificationtext_Tv.text.toString())
        addkeyskillstext_Tv.setText(keyskillstext_Tv.text.toString())
        addcareertext_Tv.setText(careertext_Tv.text.toString())
        addacheivementtext_Tv.setText(acheivementtext_Tv.text.toString())
    }

    private fun postProfessionalSummaryData() {
        val qualifications = addsummaryqualificationtext_Tv.text.toString().trim()
        val keyskills = addkeyskillstext_Tv.text.toString().trim()
        val careerObjectives = addcareertext_Tv.text.toString().trim()
        val highlights = addacheivementtext_Tv.text.toString().trim()

        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            val token = AccountUtils.getAccessToken(applicationContext)
            Log.d("Token", "Token: $token")
            showRefreshDialogue()

            val professionalBodyModel = ProfessionalSummaryBodyModel(
                qualifications = qualifications,
                keyskills = keyskills,
                carrerObjectives = careerObjectives,
                highlights = highlights
            )

            val postCall = apiService.postProfessionalSummary(token, professionalBodyModel)
            postCall.enqueue(object : Callback<ProfessionalsummaryModel> {
                override fun onResponse(
                    call: Call<ProfessionalsummaryModel>,
                    response: Response<ProfessionalsummaryModel>
                ) {
                    val statusCode = response.code()
                    Log.d("ResponseCode", "$statusCode")
               hideRefreshDialog()

                    if (response.isSuccessful) {
                        summaryqualificationtext_Tv.visibility = View.VISIBLE
                        keyskillstext_Tv.visibility = View.VISIBLE
                        careertext_Tv.visibility = View.VISIBLE
                        acheivementtext_Tv.visibility = View.VISIBLE
                        addprofessional_Tv.visibility = View.VISIBLE
                        editbtn.visibility = View.VISIBLE
                        addsummaryqualificationtext_Tv.visibility=View.GONE
                        addkeyskillstext_Tv.visibility=View.GONE
                        addcareertext_Tv.visibility=View.GONE
                        addacheivementtext_Tv.visibility=View.GONE
                        addsummaryAdd.visibility=View.GONE
                        getProfessionalSummaryData()
                        Toast.makeText(applicationContext, "Data submitted Successfully!", Toast.LENGTH_SHORT).show()

                    } else {
                        Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(applicationContext, "Failed to submit data. Error code: $statusCode", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ProfessionalsummaryModel>, t: Throwable) {
               hideRefreshDialog()
                    Log.e("onFailure", t.message ?: "Unknown error")
                    Toast.makeText(applicationContext, "Submission failed: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } catch (e: Exception) {
          hideRefreshDialog()
            Log.e("Exception", e.message ?: "Unknown error")
            Toast.makeText(applicationContext, "An error occurred: ${e.message}", Toast.LENGTH_SHORT).show()
        }
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
