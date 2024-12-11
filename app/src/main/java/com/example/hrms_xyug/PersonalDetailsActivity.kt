//package com.example.hrms_xyug
//
//import android.content.Intent
//import android.net.Uri
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.provider.MediaStore
//import android.util.Log
//import android.view.View
//import android.widget.Button
//import android.widget.ImageView
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.activity.result.contract.ActivityResultContracts
//import com.example.Interface.ServerApisInterface
//import com.example.Interface.SingleEmployeeModel
//import com.example.Interface.UploadImageModel
//import com.squareup.picasso.Picasso
//import com.xyug.hrms.AccountUtils
//import com.xyug.hrms.RetrofitClient
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.MultipartBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//class PersonalDetailsActivity : AppCompatActivity() {
//
//    private lateinit var personalsdetails_back:ImageView
//    private lateinit var savePersonalsData:Button
//    private lateinit var pictureupload_Iv:ImageView
//    private lateinit var primarytext_Tv:TextView
//    private lateinit var lastnametext_Tv:TextView
//    private lateinit var empidtext_Tv:TextView
//    private lateinit var dobtext_Tv:TextView
//    private lateinit var uploadpictext:TextView
//    private lateinit var maritaltext_Tv:TextView
//    private lateinit var nationality_Tv:TextView
//    private lateinit var department_Tv:TextView
//    private lateinit var designationtext_Tv:TextView
//    private var imageUri: Uri? = null
//    private var uploadedImageUrl: String? = null
//    private lateinit var dojtext_Tv:TextView
//    private lateinit var token:String
//    private lateinit var progressBar: ProgressBar
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_personal_details2)
//
//        progressBar = findViewById(R.id.progressBar)
//        savePersonalsData=findViewById(R.id.savePersonalsData)
//
//        savePersonalsData.setOnClickListener {
//            if (imageUri != null) {
//                uploadImageApi()
//            } else {
//                Log.e("PersonalDetailsActivity", "No image selected for upload.")
//            }
//        }
//        uploadpictext=findViewById(R.id.uploadpictext)
//
//        uploadpictext.setOnClickListener {
//            getImage.launch("image/*")
//        }
//
//        pictureupload_Iv=findViewById(R.id.pictureupload_Iv)
//        lastnametext_Tv=findViewById(R.id.lastnametext_Tv)
//        nationality_Tv=findViewById(R.id.nationality_Tv)
//        maritaltext_Tv=findViewById(R.id.maritaltext_Tv)
//        department_Tv=findViewById(R.id.department_Tv)
//        dojtext_Tv=findViewById(R.id.dojtext_Tv)
//        designationtext_Tv=findViewById(R.id.designationtext_Tv)
//        dobtext_Tv=findViewById(R.id.dobtext_Tv)
//        empidtext_Tv=findViewById(R.id.empidtext_Tv)
//        primarytext_Tv=findViewById(R.id.primarytext_Tv)
//        personalsdetails_back=findViewById(R.id.personaldetails_back)
//        personalsdetails_back.setOnClickListener {
//            val intent = Intent(this,PersonalDetails::class.java)
//            startActivity(intent)
//        }
//
//        getPersonalDetails()
//        loadImage()
//    }
//
//    private val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//        uri?.let {
//            imageUri = it
//            pictureupload_Iv.setImageURI(imageUri)
//        }
//    }
//
//    private fun uploadImageApi() {
//        progressBar.visibility = View.VISIBLE
//
//        imageUri?.let { uri ->
//            val file = File(getRealPathFromURI(uri))
//            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
//            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//            val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
//            val call = apiService.uploadImage(token, body)
//            call.enqueue(object : Callback<UploadImageModel> {
//                override fun onResponse(call: Call<UploadImageModel>, response: Response<UploadImageModel>) {
//                    val statusCode = response.code()
//
//                    Log.e("uploadimagestatuscode", "Status Code: $statusCode")
//                    progressBar.visibility = View.GONE
//
//                    if (response.isSuccessful) {
//                        val uploadImageModel = response.body()
//                        Log.i("UploadImageResponse", "Response body: $uploadImageModel")
//
//                        uploadImageModel?.let {
//                            val imageUrl = it.uploadimage?.photo
//                            if (!imageUrl.isNullOrEmpty()) {
//                                uploadedImageUrl = imageUrl
//                                AccountUtils.saveImageUrl(this@PersonalDetailsActivity, imageUrl)
//                                Picasso.get()
//                                    .load(imageUrl)
//                                    .into(pictureupload_Iv)
//
//                                saveImageUrl(imageUrl)
//
//                                Log.i("UploadImage", "Image URL: $imageUrl")
//                            } else {
//                                Log.e("UploadImage", "Image URL is null or empty")
//                            }
//                        }
//                    } else {
//                        Log.e("UploadImage", "Unexpected error: $statusCode - ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<UploadImageModel>, t: Throwable) {
//                    progressBar.visibility = View.GONE
//
//                    Log.e("UploadImage", "Upload failed: ${t.message}")
//                }
//            })
//        }
//    }
//
//    private fun saveImageUrl(imageUrl: String) {
//        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
//        editor.putString("imageUrl", imageUrl)
//        editor.apply()
//    }
//
//    private fun loadImage() {
//        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
//        val imageUrl = sharedPreferences.getString("imageUrl", null)
//        if (!imageUrl.isNullOrEmpty()) {
//            Picasso.get()
//                .load(imageUrl)
//                .into(pictureupload_Iv)
//        }
//    }
//
//    private fun getRealPathFromURI(uri: Uri): String? {
//        var path: String? = null
//        val projection = arrayOf(MediaStore.Images.Media.DATA)
//
//        val cursor = contentResolver.query(uri, projection, null, null, null)
//        cursor?.use {
//            if (it.moveToFirst()) {
//                val columnIndex = it.getColumnIndexOrThrow(projection[0])
//                path = it.getString(columnIndex)
//            }
//        }
//
//        if (path == null) {
//            path = uriToFilePath(uri)
//        }
//        return path
//    }
//
//    private fun uriToFilePath(uri: Uri): String? {
//        return try {
//            val file = File(applicationContext.cacheDir, "temp_image")
//            contentResolver.openInputStream(uri)?.use { inputStream ->
//                file.outputStream().use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//            file.absolutePath
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    private fun getPersonalDetails() {
//        progressBar.visibility = View.VISIBLE
//
//        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
//
//        try {
//            token = AccountUtils.getAccessToken(applicationContext)
//            Log.e("RawTokenregfsdgf",""+token)
//
//            val getEmployeeList = apiService.getSingleEmployee(token)
//            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
//                override fun onResponse(
//                    call: Call<SingleEmployeeModel>,
//                    response: Response<SingleEmployeeModel>) {
//                    val statusCode = response.code()
//                    Log.e("getSingleEmployeeListStatus", "$statusCode")
//                    progressBar.visibility = View.GONE
//
//                    if (statusCode == 200 || statusCode == 201) {
//                        val singleEmployeeModel = response.body()
//                        singleEmployeeModel?.let { model ->
//                            val employeeData = model.data?.employeedata
//                            val jobData = model.data?.jobdata
//
//                            if (employeeData != null) {
//                                Log.e("EmployeeData", "First Name: ${employeeData.firstName}, Last Name: ${employeeData.lastName}, Job Title: ${jobData?.jobTitle}")
//
//                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                                primarytext_Tv.text = employeeData.firstName ?: "N/A"
//                                lastnametext_Tv.text = employeeData.lastName ?: "N/A"
//                                dobtext_Tv.text = employeeData.dateOfBirth?.let { dateFormat.format(it) } ?: "N/A"
//                                empidtext_Tv.text = employeeData.employeeNumber ?: "N/A"
//                                maritaltext_Tv.text = employeeData.maritalStatus ?: "N/A"
//                              //  nationality_Tv.text = employeeData.gender ?: "N/A"
//                                dojtext_Tv.text = employeeData.dateJoined?.let { dateFormat.format(it) } ?: "N/A"
//                                designationtext_Tv.text = jobData?.jobTitle ?: "Job title not available"
//                                department_Tv.text = jobData?.departmentName ?: "N/A"
//
//                                val imageUrl = employeeData.photo
//                                if (!imageUrl.isNullOrEmpty()) {
//                                    Picasso.get()
//                                        .load(imageUrl)
//                                        .into(pictureupload_Iv)
//                                } else {
//                                    pictureupload_Iv.setImageResource(R.drawable.uploadpicture1)
//                                }
//
//                            } else {
//                                Log.e("Error", "EmployeeData is null")
//                            }
//                        }
//                    } else {
//                        Log.e("ErrorResponse", response.errorBody()?.string() ?: "No error body")
//                    }
//                }
//
//                override fun onFailure(call: Call<SingleEmployeeModel>, t: Throwable) {
//                    progressBar.visibility = View.GONE
//                    Log.e("onFailure", t.toString())
//                }
//            })
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//    override fun onBackPressed() {
//        val intent = Intent(this,PersonalDetails::class.java)
//        startActivity(intent)
//    }
//}

package com.example.hrms_xyug

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentUris
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.Interface.ServerApisInterface
import com.example.Interface.SingleEmployeeModel
import com.example.Interface.UploadImageModel
import com.squareup.picasso.Picasso
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import com.soundcloud.android.crop.Crop
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class PersonalDetailsActivity : AppCompatActivity() {

    private lateinit var personalsdetails_back: ImageView
    private lateinit var savePersonalsData: Button
    private lateinit var pictureupload_Iv: ImageView
    private lateinit var primarytext_Tv: TextView
    private lateinit var lastnametext_Tv: TextView
    private lateinit var empidtext_Tv: TextView
    private lateinit var dobtext_Tv: TextView
    private lateinit var uploadpictext: TextView
    private lateinit var maritaltext_Tv: TextView
    private lateinit var nationality_Tv: TextView
    private lateinit var department_Tv: TextView
    private lateinit var designationtext_Tv: TextView
    private var imageUri: Uri? = null
    private var uploadedImageUrl: String? = null
    private lateinit var dojtext_Tv: TextView
    private lateinit var token: String
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog

    companion object {
        const val REQUEST_CODE_PERMISSIONS = 101
        const val REQUEST_CODE_PICK_IMAGE = 102
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_details2)

        initViews()
        getPersonalDetails()
        loadImage()
        requestPermissions()
        setupListeners()
    }

    private fun initViews() {
        savePersonalsData = findViewById(R.id.savePersonalsData)
        uploadpictext = findViewById(R.id.uploadpictext)
        pictureupload_Iv = findViewById(R.id.pictureupload_Iv)
        pictureupload_Iv.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }
        lastnametext_Tv = findViewById(R.id.lastnametext_Tv)
        nationality_Tv = findViewById(R.id.nationality_Tv)
        maritaltext_Tv = findViewById(R.id.maritaltext_Tv)
        department_Tv = findViewById(R.id.department_Tv)
        dojtext_Tv = findViewById(R.id.dojtext_Tv)
        designationtext_Tv = findViewById(R.id.designationtext_Tv)
        dobtext_Tv = findViewById(R.id.dobtext_Tv)
        empidtext_Tv = findViewById(R.id.empidtext_Tv)
        primarytext_Tv = findViewById(R.id.primarytext_Tv)
        personalsdetails_back = findViewById(R.id.personaldetails_back)
    }

    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
        }
    }

    private fun setupListeners() {
        savePersonalsData.setOnClickListener {
            if (imageUri != null) {
                uploadImageApi()
            } else {
                Toast.makeText(this, "Please select an image before uploading.", Toast.LENGTH_SHORT).show()
                Log.e("PersonalDetailsActivity", "No image selected for upload.")

            }
        }
        uploadpictext.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
        }

        personalsdetails_back.setOnClickListener {
            val intent = Intent(this, PersonalDetails::class.java)
            startActivity(intent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
               // Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
               // Toast.makeText(this, "Permissions not granted", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_PICK_IMAGE -> {
                    val uri = data?.data
                    uri?.let {
                        val croppedFile = File(cacheDir, "cropped_${System.currentTimeMillis()}.jpg") // Unique filename
                        val croppedUri = FileProvider.getUriForFile(
                            this,
                            "com.example.hrms_xyug.fileprovider",
                            croppedFile
                        )
                        Crop.of(it, croppedUri).asSquare().start(this)
                    }
                }

                Crop.REQUEST_CROP -> {
                    val uri = Crop.getOutput(data)
                    if (uri != null) {
                        imageUri = uri
                        Log.i("ImageCrop", "Cropped image URI: $uri")
                        Picasso.get().invalidate(uri)
                        pictureupload_Iv.setImageDrawable(null)
                        Picasso.get().load(uri).into(pictureupload_Iv)
                    } else {
                        Log.e("ImageCrop", "Crop failed, URI is null.")
                    }
                }
            }
        }
    }


    private fun getFileFromUri(uri: Uri): File? {
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val file = File.createTempFile("temp", ".jpg", cacheDir)
            inputStream?.use { input ->
                FileOutputStream(file).use { output ->
                    val buffer = ByteArray(1024)
                    var read: Int
                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                    }
                }
            }
            file
        } catch (e: IOException) {
            Log.e("FileHandling", "Error creating file from URI", e)
            null
        }
    }

    private fun uploadImageApi() {
        showRefreshDialogue()
        imageUri?.let { uri ->
            val file = getFileFromUri(uri)
            if (file != null && file.exists()) {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
                val call = apiService.uploadImage(token, body)
                call.enqueue(object : Callback<UploadImageModel> {
                    override fun onResponse(call: Call<UploadImageModel>, response: Response<UploadImageModel>) {
                        hideRefreshDialog()

                        if (response.isSuccessful) {
                            val uploadImageModel = response.body()
                            Log.i("UploadImageResponse", "Response body: $uploadImageModel")

                            uploadImageModel?.let {
                                val imageUrl = it.uploadimage?.photo
                                if (!imageUrl.isNullOrEmpty()) {
                                    uploadedImageUrl = imageUrl
                                    AccountUtils.saveImageUrl(this@PersonalDetailsActivity, imageUrl)
                                    Picasso.get().load(imageUrl).into(pictureupload_Iv)
                                    saveImageUrl(imageUrl)
                                    Log.i("UploadImage", "Image URL: $imageUrl")
                                    pictureupload_Iv.setImageDrawable(null)
                                    imageUri = null
                                } else {
                                    Log.e("UploadImage", "Image URL is null or empty")
                                }
                            }
                        } else {
                            Log.e("UploadImage", "Unexpected error: ${response.code()} - ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<UploadImageModel>, t: Throwable) {
                        hideRefreshDialog()
                        Log.e("UploadImage", "Upload failed: ${t.message}")
                    }
                })
            } else {
                Log.e("UploadImage", "File does not exist or could not be created: ${file?.absolutePath}")
            }
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

    private fun saveImageUrl(imageUrl: String) {
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("image_url", imageUrl)
        editor.apply()
    }

    private fun loadImage() {
        val imageUrl = AccountUtils.getImageUrl(this)
        if (!imageUrl.isNullOrEmpty()) {
            hideRefreshDialog()
            Picasso.get()
                .load(imageUrl)
                .into(pictureupload_Iv)
        } else {
            Log.e("LoadImage", "Image URL is null or empty")
        }
    }

    private fun getPersonalDetails() {
        showRefreshDialogue()
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
        try {
            token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawTokenregfsdgf",""+token)

            val getEmployeeList = apiService.getSingleEmployee(token)
            getEmployeeList.enqueue(object : Callback<SingleEmployeeModel> {
                override fun onResponse(
                    call: Call<SingleEmployeeModel>, response: Response<SingleEmployeeModel>) {
                    hideRefreshDialog()
                    val statusCode = response.code()
                    Log.e("getSingleEmployeeListStatus", "$statusCode")

                    if (statusCode == 200 || statusCode == 201) {
                        val singleEmployeeModel = response.body()
                        singleEmployeeModel?.let { model ->
                            val employeeData = model.data?.employeedata
                            val jobData = model.data?.jobdata

                            if (employeeData != null) {
                                Log.e("EmployeeData", "First Name: ${employeeData.firstName}, Last Name: ${employeeData.lastName}, Job Title: ${jobData?.jobTitle}")

                                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                primarytext_Tv.text = employeeData.firstName ?: "N/A"
                                lastnametext_Tv.text = employeeData.lastName ?: "N/A"
                                dobtext_Tv.text = employeeData.dateOfBirth?.let { dateFormat.format(it) } ?: "N/A"
                                empidtext_Tv.text = employeeData.employeeNumber ?: "N/A"
                                maritaltext_Tv.text = employeeData.maritalStatus ?: "N/A"
                                //  nationality_Tv.text = employeeData.gender ?: "N/A"
                                dojtext_Tv.text = employeeData.dateJoined?.let { dateFormat.format(it) } ?: "N/A"
                                designationtext_Tv.text = jobData?.jobTitle ?: "Job title not available"
                                department_Tv.text = jobData?.departmentName ?: "N/A"

                                val imageUrl = employeeData.photo
                                if (!imageUrl.isNullOrEmpty()) {
                                    Picasso.get()
                                        .load(imageUrl)
                                        .into(pictureupload_Iv)
                                } else {
                                    pictureupload_Iv.setImageResource(R.drawable.uploadpicture)
                                }

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

    private fun getRealPathFromURI(uri: Uri): String? {
        var filePath: String? = null

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = contentResolver.query(uri, projection, null, null, null)

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex != -1) {
                    filePath = it.getString(columnIndex)
                }
            }
        }

        if (filePath == null) {
            filePath = uri.path
        }

        Log.d("FilePath", "File path: $filePath")
        return filePath
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
