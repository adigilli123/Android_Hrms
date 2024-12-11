package com.example.hrms_xyug

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.AllEmpAdapter
import com.example.Interface.AllEmployeeModel
import com.example.Interface.ServerApisInterface
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
class AllEmployeesActivity : AppCompatActivity() {

    private lateinit var allemp_recycler: RecyclerView
    private lateinit var allemp_back: ImageView
    private lateinit var search_text: EditText
    private lateinit var crossicon: ImageView
    private lateinit var adapter: AllEmpAdapter
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_employees)

//        menubtn.setOnClickListener {
//            val intent = Intent(this, HirarciStructureAcrivity::class.java)
//         startActivity(intent)
//        }

        allemp_recycler = findViewById(R.id.allemp_recycler)
        allemp_back = findViewById(R.id.allemp_back)
        search_text = findViewById(R.id.search_text)
        crossicon = findViewById(R.id.crossicon)

        allemp_back.setOnClickListener {
            navigateToProfileFragment()
        }

        crossicon.setOnClickListener {
            search_text.setText("")
        }

        search_text.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (::adapter.isInitialized) {
                    adapter.filter(s.toString())
                }
                crossicon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        getAllEmployee()
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

    private fun getAllEmployee() {
        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        try {
            val token = AccountUtils.getAccessToken(applicationContext)
            Log.e("RawTokenregfsdgf", "" + token)
            showRefreshDialogue()

            val getEmployeeList = apiService.getAllEmployeess(token)
            getEmployeeList.enqueue(object : Callback<List<AllEmployeeModel>> {
                override fun onResponse(
                    call: Call<List<AllEmployeeModel>>,
                    response: Response<List<AllEmployeeModel>>
                ) {
                    val statusCode = response.code()
                    Log.e("getEmployeeListStatus", "$statusCode")
                  hideRefreshDialog()

                    if (statusCode == 200 || statusCode == 201) {
                        val employeeList = response.body()
                        employeeList?.let {
                            setupRecyclerView(this@AllEmployeesActivity, it)
                        }
                    } else {
                        Log.e("ErrorResponse", response.errorBody()?.string() ?: "No error body")
                    }
                }

                override fun onFailure(call: Call<List<AllEmployeeModel>>, t: Throwable) {
                hideRefreshDialog()
                    Log.e("onFailure", t.toString())
                }
            })
        } catch (e: Exception) {
          hideRefreshDialog()
            e.printStackTrace()
        }
    }

    private fun setupRecyclerView(context: Context, employeeList: List<AllEmployeeModel>) {
        adapter = AllEmpAdapter(context, employeeList)
        allemp_recycler.layoutManager = LinearLayoutManager(applicationContext)
        allemp_recycler.adapter = adapter
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
