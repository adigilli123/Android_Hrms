package com.example.hrms_xyug

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.Interface.EducationdetailsModel
import com.example.Interface.ServerApisInterface
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar


class AddEducationActivity : AppCompatActivity() {
    private lateinit var educationBtn:Button
    private lateinit var addeducationimagetext_Tv:ImageView
    private lateinit var addinstitutetext_Tv:EditText
    private lateinit var addgradetext_Tv:EditText
    private lateinit var addqualificationtext_Tv:EditText
    private lateinit var addcoursetext_Tv:EditText
    private lateinit var addyoptext_Tv:TextView
    private lateinit var eduaction_back:ImageView
    private lateinit var educationclickimage_Iv:ImageView
    private var selectedImagePath: String? = null
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private lateinit var extra_textview1: TextView
    private lateinit var instituteerrormessage: TextView
    private lateinit var gradeerrormessage: TextView
    private lateinit var qualificationerrormessage: TextView
    private lateinit var courseerrormessage: TextView
    private lateinit var yoperrormessage: TextView
    private lateinit var scrollview_education: ScrollView
    private lateinit var primaryProfRelative:RelativeLayout


    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_education)

        primaryProfRelative=findViewById(R.id.primaryProfRelative)
        scrollview_education=findViewById(R.id.scrollview_education)
        extra_textview1=findViewById(R.id.extra_textview1)
        addyoptext_Tv=findViewById(R.id.addyoptext_Tv)

        addyoptext_Tv.setOnClickListener {
                    val calendar = Calendar.getInstance()
                    val currentYear = calendar.get(Calendar.YEAR)
                    instituteerrormessage.visibility = View.GONE
                    gradeerrormessage.visibility = View.GONE
                    qualificationerrormessage.visibility = View.GONE
                    yoperrormessage.visibility = View.GONE
                    courseerrormessage.visibility = View.GONE
                    val inflater = LayoutInflater.from(this)
                    val view = inflater.inflate(R.layout.dialog_year_picker, null)

                    val yearPicker = view.findViewById<NumberPicker>(R.id.year_picker)

                    yearPicker.minValue = 1900
                    yearPicker.maxValue = currentYear
                    yearPicker.value = currentYear

                    AlertDialog.Builder(this)
                        .setTitle("Select Year")
                        .setView(view)
                        .setPositiveButton("OK") { _, _ ->
                            // Set the selected year in addyoptext_Tv
                            addyoptext_Tv.text = yearPicker.value.toString()
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
        educationclickimage_Iv=findViewById(R.id.educationclickimage_Iv)
        eduaction_back=findViewById(R.id.eduaction_back)
        eduaction_back.setOnClickListener {
            onBackPressed()
        }

        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                extra_textview1.visibility = View.INVISIBLE
            } else {
                extra_textview1.visibility = View.GONE
            }
        }


        instituteerrormessage=findViewById(R.id.instituteerrormessage)
        gradeerrormessage=findViewById(R.id.gradeerrormessage)
        qualificationerrormessage=findViewById(R.id.qualificationerrormessage)
        courseerrormessage=findViewById(R.id.courseerrormessage)
        yoperrormessage=findViewById(R.id.yoperrormessage)

        addcoursetext_Tv=findViewById(R.id.addcoursetext_Tv)
        addcoursetext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                instituteerrormessage.visibility = View.GONE
                gradeerrormessage.visibility = View.GONE
                qualificationerrormessage.visibility = View.GONE
                yoperrormessage.visibility = View.GONE
                courseerrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        addqualificationtext_Tv=findViewById(R.id.addqualificationtext_Tv)
        addqualificationtext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                instituteerrormessage.visibility = View.GONE
                gradeerrormessage.visibility = View.GONE
                qualificationerrormessage.visibility = View.GONE
                yoperrormessage.visibility = View.GONE
                courseerrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        addgradetext_Tv=findViewById(R.id.addgradetext_Tv)
        addgradetext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                instituteerrormessage.visibility = View.GONE
                gradeerrormessage.visibility = View.GONE
                qualificationerrormessage.visibility = View.GONE
                yoperrormessage.visibility = View.GONE
                courseerrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        addinstitutetext_Tv=findViewById(R.id.addinstitutetext_Tv)
        addinstitutetext_Tv.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                instituteerrormessage.visibility = View.GONE
                gradeerrormessage.visibility = View.GONE
                qualificationerrormessage.visibility = View.GONE
                yoperrormessage.visibility = View.GONE
                courseerrormessage.visibility = View.GONE
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        addeducationimagetext_Tv=findViewById(R.id.addeducationimagetext_Tv)
        educationclickimage_Iv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        educationBtn=findViewById(R.id.educationBtn)
        educationBtn.setOnClickListener {
            val institutionText = addinstitutetext_Tv.text.toString().trim()
            val yearOfPassing = addyoptext_Tv.text.toString().trim()
            val qualificationText = addqualificationtext_Tv.text.toString().trim()
            val gradeText = addgradetext_Tv.text.toString().trim()
            val courseText = addcoursetext_Tv.text.toString().trim()

            instituteerrormessage.text = ""
            instituteerrormessage.visibility = View.GONE
            gradeerrormessage.text = ""
            gradeerrormessage.visibility = View.GONE
            qualificationerrormessage.text = ""
            qualificationerrormessage.visibility = View.GONE
            yoperrormessage.text = ""
            yoperrormessage.visibility = View.GONE
            courseerrormessage.text = ""
            courseerrormessage.visibility = View.GONE

            if (institutionText.isEmpty()) {
                instituteerrormessage.text = "Please Enter Institution name"
                instituteerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, instituteerrormessage.top)
                addinstitutetext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (institutionText.length < 3) {
                instituteerrormessage.text = "Please Enter Institution name must be at least 3 characters"
                instituteerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, instituteerrormessage.top)
                addinstitutetext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (gradeText.isEmpty()) {
                gradeerrormessage.text = "Please Enter GPA/Grade"
                gradeerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, gradeerrormessage.top)
                addgradetext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (gradeText.length < 1) {
                gradeerrormessage.text = "Please Enter Valid GPA/Grade"
                gradeerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, gradeerrormessage.top)
                addgradetext_Tv.requestFocus()
                return@setOnClickListener
            }
            val gradePattern = Regex("^([0-9]{1,2})(\\.\\d{1,3})?\$")
            if (!gradePattern.matches(gradeText)) {
                gradeerrormessage.text = "Please Enter valid number (e.g., 8.6, 98.6, 8.778)"
                gradeerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, gradeerrormessage.top)
                addgradetext_Tv.requestFocus()
                return@setOnClickListener
            }
            val gradeValue = gradeText.toDoubleOrNull()
            if (gradeValue == null || gradeValue < 0.0 || gradeValue > 100.0) {
                gradeerrormessage.text = "Please Enter GPA/Grade must be between 0 and 100"
                gradeerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, gradeerrormessage.top)
                addgradetext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (qualificationText.isEmpty()) {
                qualificationerrormessage.text = "Please Enter Qualification"
                qualificationerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, qualificationerrormessage.top)
                addqualificationtext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (qualificationText.length < 2) {
                qualificationerrormessage.text = "Please Enter Qualification must be at least 3 characters"
                qualificationerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, qualificationerrormessage.top)
                addqualificationtext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (courseText.isNotEmpty() && courseText.length < 3) {
                courseerrormessage.text = "Please Enter Course name must be at least 3 characters if entered"
                courseerrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, yoperrormessage.top)
                addcoursetext_Tv.requestFocus()
                return@setOnClickListener
            }
            if (yearOfPassing.isEmpty()) {
                yoperrormessage.text = "Please Select Year of Passing"
                yoperrormessage.visibility = View.VISIBLE
                scrollview_education.smoothScrollTo(0, yoperrormessage.top)
                return@setOnClickListener
            }
            addinstitutetext_Tv.setText(institutionText)
            addyoptext_Tv.setText(yearOfPassing)
            addqualificationtext_Tv.setText(qualificationText)
            addgradetext_Tv.setText(gradeText)
            postEducationData()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let { uri ->
                val filePath = getPathFromUri(uri)
                selectedImagePath = filePath
                addeducationimagetext_Tv.setImageURI(uri)
            }
        }
    }

    private fun postEducationData() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            val token = AccountUtils.getAccessToken(applicationContext)
            Log.d("Token", "Token: $token")
            showRefreshDialogue()

            val yearOfPassing = RequestBody.create("text/plain".toMediaTypeOrNull(), addyoptext_Tv.text.toString())
            val specialization = RequestBody.create("text/plain".toMediaTypeOrNull(), addcoursetext_Tv.text.toString())
            val degree = RequestBody.create("text/plain".toMediaTypeOrNull(), addqualificationtext_Tv.text.toString())
            val grade = RequestBody.create("text/plain".toMediaTypeOrNull(), addgradetext_Tv.text.toString())
            val institution = RequestBody.create("text/plain".toMediaTypeOrNull(), addinstitutetext_Tv.text.toString())

            val imageFile = selectedImagePath?.let { File(it) }
            val requestBody = imageFile?.let { RequestBody.create("image/*".toMediaTypeOrNull(), it) }
            val imagePart = requestBody?.let { MultipartBody.Part.createFormData("image", imageFile.name, it) }

            val postCall = apiService.postEducationDetails(token, degree, institution, yearOfPassing, specialization, grade, imagePart)
            postCall.enqueue(object : Callback<EducationdetailsModel> {
                override fun onResponse(
                    call: Call<EducationdetailsModel>,
                    response: Response<EducationdetailsModel>
                ) {
                    val statusCode = response.code()
                    Log.d("ResponseCode", "$statusCode")
                  hideRefreshDialog()

                    if (response.isSuccessful) {
                        val educationDetails = response.body()
                        Toast.makeText(applicationContext, "Data submitted successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AddEducationActivity, EducationDetailsActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.e("ResponseError", "Error: ${response.errorBody()?.string()}")
                        Toast.makeText(applicationContext, "Failed to submit data. Error code: $response", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<EducationdetailsModel>, t: Throwable) {
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
        val intent=Intent(this,EducationDetailsActivity::class.java)
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