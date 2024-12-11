package com.example.hrms_xyug

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.example.Interface.ExpereincedetailsModel
import com.example.Interface.ServerApisInterface
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExperienceActivity : AppCompatActivity() {

    private lateinit var addexperiencedetails_back:ImageView
    private lateinit var addompanyname_Tv:EditText
    private lateinit var addstartdate_Tv:TextView
    private lateinit var addjobtitlee_Tv:EditText
    private lateinit var addenddatetext_Tv1:TextView
    private lateinit var addresponsibilities1_Tv:EditText
    private lateinit var addacheivements1_Tv:EditText
    private lateinit var addskills_Tv:EditText
    private lateinit var addexperienceDocument:ImageView
    private lateinit var clickimage_Iv:ImageView
    private lateinit var addExperince:Button
    private lateinit var starttime_Tv:TextView
    private lateinit var scrollView: ScrollView
    private lateinit var endtime_Tv:TextView
    private var selectedImagePath: String? = null
    private lateinit var imageView: ImageView

    private lateinit var joberrormessage: TextView
    private lateinit var companyerrormessage: TextView
    private lateinit var startdateerrormessage: TextView
    private lateinit var enddateerrormessage: TextView
    private lateinit var jonresponseerrormessage: TextView
    private lateinit var achievementerrormessage: TextView
    private lateinit var skillserrormessage: TextView

    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private var startDateMillis: Long? = null
    private var endDateMillis: Long? = null
    data class DateSelection(val date: String, val millis: Long)

    private lateinit var extra_textview: TextView
    companion object {
        private const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_experience)

        addexperiencedetails_back=findViewById(R.id.addexperiencedetails_back)
        addexperiencedetails_back.setOnClickListener {
            onBackPressed()
        }

        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                extra_textview.visibility = View.INVISIBLE
            } else {
                extra_textview.visibility = View.GONE
            }
        }
        scrollView=findViewById(R.id.scrollview_exp)
        joberrormessage=findViewById(R.id.joberrormessage)
        companyerrormessage=findViewById(R.id.companyerrormessage)
        startdateerrormessage=findViewById(R.id.startdateerrormessage)
        enddateerrormessage=findViewById(R.id.enddateerrormessage)
        jonresponseerrormessage=findViewById(R.id.jonresponseerrormessage)
        achievementerrormessage=findViewById(R.id.achievementerrormessage)
        skillserrormessage=findViewById(R.id.skillserrormessage)
        extra_textview=findViewById(R.id.extra_textview)
        addompanyname_Tv=findViewById(R.id.addompanyname_Tv)

        addompanyname_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                companyerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        clickimage_Iv=findViewById(R.id.clickimage_Iv)
        addstartdate_Tv=findViewById(R.id.addstartdate_Tv)
        starttime_Tv=findViewById(R.id.starttime_Tv)
        endtime_Tv=findViewById(R.id.endtime_Tv)

        starttime_Tv.setOnClickListener {
            showDatePickerDialog { dateSelection ->
                addstartdate_Tv.text = dateSelection.date
                startDateMillis = dateSelection.millis
                companyerrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }
        }

        endtime_Tv.setOnClickListener {
            startDateMillis?.let { startMillis ->
                showDatePickerDialog(minDateMillis = startMillis) { dateSelection ->
                    addenddatetext_Tv1.text = dateSelection.date
                    endDateMillis = dateSelection.millis
                    companyerrormessage.visibility = View.GONE
                    startdateerrormessage.visibility = View.GONE
                    enddateerrormessage.visibility = View.GONE
                    joberrormessage.visibility = View.GONE
                    jonresponseerrormessage.visibility = View.GONE
                    achievementerrormessage.visibility = View.GONE
                    skillserrormessage.visibility = View.GONE
                }
            } ?: run {

                Toast.makeText(this, "Please select a start date first", Toast.LENGTH_SHORT).show()
            }
        }

        addjobtitlee_Tv=findViewById(R.id.addjobtitlee_Tv)
        addjobtitlee_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                companyerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addenddatetext_Tv1=findViewById(R.id.addenddatetext_Tv1)
        addresponsibilities1_Tv=findViewById(R.id.addresponsibilities1_Tv)
        addresponsibilities1_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                companyerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addacheivements1_Tv=findViewById(R.id.addacheivements1_Tv)
        addacheivements1_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                companyerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addskills_Tv=findViewById(R.id.addskills_Tv)
        addskills_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                companyerrormessage.visibility = View.GONE
                startdateerrormessage.visibility = View.GONE
                joberrormessage.visibility = View.GONE
                enddateerrormessage.visibility = View.GONE
                jonresponseerrormessage.visibility = View.GONE
                achievementerrormessage.visibility = View.GONE
                skillserrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })
        addexperienceDocument=findViewById(R.id.addexperienceDocument)
        addExperince=findViewById(R.id.addExperince)
        addExperince.setOnClickListener {
            val jobTitle = addjobtitlee_Tv.text.toString().trim()
            val endDate = addenddatetext_Tv1.text.toString().trim()
            val startDate = addstartdate_Tv.text.toString().trim()
            val companyName = addompanyname_Tv.text.toString().trim()
            val jonResponse = jonresponseerrormessage.text.toString().trim()
            val achievement = achievementerrormessage.text.toString().trim()
            val skills = skillserrormessage.text.toString().trim()

            joberrormessage.text = ""
            joberrormessage.visibility = View.GONE
            companyerrormessage.text = ""
            companyerrormessage.visibility = View.GONE
            startdateerrormessage.text = ""
            startdateerrormessage.visibility = View.GONE
            enddateerrormessage.text = ""
            enddateerrormessage.visibility = View.GONE
            jonresponseerrormessage.text = ""
            jonresponseerrormessage.visibility = View.GONE
            achievementerrormessage.text = ""
            achievementerrormessage.visibility = View.GONE
            skillserrormessage.text = ""
            skillserrormessage.visibility = View.GONE

            if (jobTitle.isEmpty()) {
                joberrormessage.text = "Please Enter Job title"
                joberrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, joberrormessage.top)
                addjobtitlee_Tv.requestFocus()
                return@setOnClickListener
            }
            if (jobTitle.length < 3) {
                joberrormessage.text = "Please Enter Job title must be at least 3 characters"
                joberrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, joberrormessage.top)
                addjobtitlee_Tv.requestFocus()
                return@setOnClickListener
            }
            if (companyName.isEmpty()) {
                companyerrormessage.text = "Please Enter Company name"
                companyerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, companyerrormessage.top)
                addompanyname_Tv.requestFocus()
                return@setOnClickListener
            }
            if (companyName.length < 3) {
                companyerrormessage.text = "Please Enter Company name must be at least 3 characters"
                companyerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, companyerrormessage.top)
                addompanyname_Tv.requestFocus()
                return@setOnClickListener
            }
            if (startDate.isEmpty()) {
                startdateerrormessage.text = "Please Select Start date"
                startdateerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, startdateerrormessage.top)
                return@setOnClickListener
            }
            if (endDate.isEmpty()) {
                enddateerrormessage.text = "Please Select End date"
                enddateerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, enddateerrormessage.top)
                return@setOnClickListener
            }
            if (jonResponse.isNotEmpty() && jonResponse.length < 3) {
                jonresponseerrormessage.text = "Please Enter Job response must be at least 3 characters"
                jonresponseerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, jonresponseerrormessage.top)
                jonresponseerrormessage.requestFocus()
                return@setOnClickListener
            }
            if (achievement.isNotEmpty() && achievement.length < 3) {
                achievementerrormessage.text = "Please Enter Achievement must be at least 3 characters"
                achievementerrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, achievementerrormessage.top)
                achievementerrormessage.requestFocus()
                return@setOnClickListener
            }
            if (skills.isNotEmpty() && skills.length < 3) {
                skillserrormessage.text = "Please Enter Skills must be at least 3 characters"
                skillserrormessage.visibility = View.VISIBLE
                scrollView.smoothScrollTo(0, skillserrormessage.top)
                skillserrormessage.requestFocus()
                return@setOnClickListener
            }

            postExperienceData()
        }
        clickimage_Iv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, AddEducationActivity.REQUEST_CODE_PICK_IMAGE)
        }
    }
    private fun showDatePickerDialog(
        minDateMillis: Long? = null,
        onDateSelected: (DateSelection) -> Unit) {
        val calendar = Calendar.getInstance()
         val datePickerDialog = DatePickerDialog(
            this,
            R.style.CustomDatePicker,
            { _, year, month, dayOfMonth ->
                val selectedDate = formatDate(year, month, dayOfMonth)
                val selectedDateMillis = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.timeInMillis
                onDateSelected(DateSelection(selectedDate, selectedDateMillis))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))

        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
        minDateMillis?.let {
            datePickerDialog.datePicker.minDate = it
        }

        datePickerDialog.show()
    }

    private fun formatDate(year: Int, month: Int, dayOfMonth: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AddEducationActivity.REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let { uri ->
                val filePath = getPathFromUri(uri)
                selectedImagePath = filePath
                addexperienceDocument.setImageURI(uri)
            }
        }
    }

    private fun postExperienceData() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            val token = AccountUtils.getAccessToken(applicationContext)
            Log.d("Token", "Token: $token")
            showRefreshDialogue()

            val companyNameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addompanyname_Tv.text.toString())
            val roleBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addjobtitlee_Tv.text.toString())
            val startDateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addstartdate_Tv.text.toString())
            val endDateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addenddatetext_Tv1.text.toString())
            val achievementsBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addacheivements1_Tv.text.toString())
            Log.e("achievementsBody",""+achievementsBody)
            val jobResponsibilityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addresponsibilities1_Tv.text.toString())
            val skillsBody = RequestBody.create("text/plain".toMediaTypeOrNull(), addskills_Tv.text.toString())

            val imageFile = selectedImagePath?.let { File(it) }
            val imagePart = imageFile?.let {
                val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), it)
                MultipartBody.Part.createFormData("image", it.name, requestFile)
            }

            val postCall = apiService.postExperienceDetails(
                token = token,
                companyName = companyNameBody,
                role = roleBody,
                startDate = startDateBody,
                endDate = endDateBody,
                image = imagePart,
                achievements = achievementsBody,
                jobResponsibility = jobResponsibilityBody,
                skills = skillsBody
            )

            postCall.enqueue(object : Callback<ExpereincedetailsModel> {
                override fun onResponse(
                    call: Call<ExpereincedetailsModel>,
                    response: Response<ExpereincedetailsModel>
                ) {
                    val statusCode = response.code()
                    Log.d("ResponseCode", "$statusCode")
                  hideRefreshDialog()

                    if (response.isSuccessful) {
                        val experienceDetails = response.body()
                        Toast.makeText(applicationContext, "Data submitted successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddExperienceActivity, ExperienceDetailsActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(applicationContext, "Failed to submit data. Error code: $statusCode", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ExpereincedetailsModel>, t: Throwable) {
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


    private fun getPathFromUri(uri: Uri): String? {
        var filePath: String? = null
        val cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                filePath = it.getString(columnIndex)
            }
            it.close()
        }
        return filePath
    }


    override fun onBackPressed() {
        val intent= Intent(this,ExperienceDetailsActivity::class.java)
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