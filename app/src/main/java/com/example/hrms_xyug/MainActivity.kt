package com.example.hrms_xyug

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.Interface.SendOtpModel
import com.example.Interface.ServerApisInterface
import com.xyug.hrms.AccountUtils
import okhttp3.OkHttpClient
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class MainActivity : AppCompatActivity() {
    private lateinit var st_phonenumber: String
    private lateinit var etmobile: EditText
    private lateinit var btn_login: Button
    private var backPressed: Long = 0
    private val TIME_DELAY: Long = 2000
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var dialog1: Dialog
    private lateinit var extra_textview4: TextView

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.white)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        init()
    }

    private fun init() {
        etmobile = findViewById(R.id.et_emailmobile)
        btn_login = findViewById(R.id.btn_login)
        extra_textview4 = findViewById(R.id.extra_textview4)

        etmobile.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(name: Editable) {
                st_phonenumber = name.toString()

            }
        })

        val rootView = window.decorView.findViewById<View>(android.R.id.content)
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            rootView.getWindowVisibleDisplayFrame(r)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) {
                extra_textview4.visibility = View.INVISIBLE
            } else {
                extra_textview4.visibility = View.GONE
            }
        }

        btn_login.setOnClickListener {
            st_phonenumber = etmobile.text.toString()

            if (st_phonenumber.isEmpty()) {
                Toast.makeText(applicationContext, "Please enter the phone number", Toast.LENGTH_SHORT).show()
            }
            else if (st_phonenumber.length < 10) {
                Toast.makeText(applicationContext, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
            }
            else {
                if (isConn()) {
                    loginData(st_phonenumber)
                } else {
                    Toast.makeText(applicationContext, "No Internet Connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed()
            finish()
        } else {
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show()
            backPressed = System.currentTimeMillis()
        }
    }

    private fun loginData(mobile: String) {
        showRefreshDialogue()
        val client = OkHttpClient.Builder()
            .sslSocketFactory(getUnsafeSslSocketFactory(), getUnsafeTrustManager() as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(ServerApisInterface.home_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        val apiInterface = retrofit.create(ServerApisInterface::class.java)

        val paramObject = JSONObject().apply {
            put("mobile", mobile)
        }

        val postlogin = apiInterface.sendotp(paramObject.toString())

        postlogin.enqueue(object : Callback<SendOtpModel> {
            override fun onResponse(
                call: retrofit2.Call<SendOtpModel>,
                response: retrofit2.Response<SendOtpModel>) {
                val statusCode = response.code()
                Log.e("LoginStatus", "$statusCode")
                hideRefreshDialog()
                if (response.isSuccessful && (statusCode == 200 || statusCode == 201)) {
                    val list = response.body()
                    if (list != null) {
                        val intent = Intent(applicationContext, OtpActivity::class.java).apply {
                            putExtra("EXTRA_MOBILE_NUMBER", mobile)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Login response is empty", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()?.string() ?: "")
                        val errorMessage = jObjError.getString("message")
                        Log.e("ErrorMessage", errorMessage)
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Error parsing response", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Error parsing response", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: retrofit2.Call<SendOtpModel>, t: Throwable) {
                hideRefreshDialog()
                Log.e("LoginStatus", "Request failed: ${t.message ?: "Unknown error"}")
                Toast.makeText(applicationContext, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUnsafeSslSocketFactory(): SSLSocketFactory {
        return try {
            val trustAllCerts: Array<TrustManager> = arrayOf(
                object : X509TrustManager {
                    override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                    override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
                    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                        return arrayOf()
                    }
                })

            val sslContext: SSLContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            sslContext.socketFactory
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getUnsafeTrustManager(): TrustManager {
        return object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun checkServerTrusted(chain: Array<java.security.cert.X509Certificate>, authType: String) {}
            override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                return arrayOf()
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

    private fun isConn(): Boolean {
        val connectivity = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivity.activeNetworkInfo?.isConnected ?: false
    }
}