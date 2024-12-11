package com.example.hrms_xyug

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chaos.view.PinView
import com.example.Interface.SendOtpModel
import com.example.Interface.ServerApisInterface
import com.example.Interface.VerifyOtp
import com.example.Interface.VerifyOtpBodyModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
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

class OtpActivity : AppCompatActivity() {
    private lateinit var otpbackbtn: RelativeLayout
    private lateinit var otpBtn: Button
    private lateinit var pinView: PinView
    private lateinit var linearOtpThree: LinearLayout
    private lateinit var resendtimeOtpTv: TextView
    private lateinit var timeOtpTv: TextView
    private lateinit var resendTimer: CountDownTimer
    private lateinit var otpNumberTv:TextView
    private lateinit var mobile: String
    private lateinit var otp: String
    private lateinit var otpErrorTextView:TextView
    private lateinit var token: String
    private lateinit var dialog1: Dialog
    private lateinit var imageView: ImageView
    private val handler = Handler()
    private lateinit var addressdetails_back:ImageView

    private companion object {
        private const val OTP_TIMER_DURATION_MS: Long = 120000
        private const val RESEND_TIMER_DURATION_MS: Long = 120000
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        resendtimeOtpTv = findViewById(R.id.resendtimeOtpTv)
        timeOtpTv = findViewById(R.id.timeOtpTv)
        otpNumberTv=findViewById(R.id.otpNumberTv)
        otpErrorTextView = findViewById(R.id.otpErrorTextView)
        addressdetails_back=findViewById(R.id.addressdetails_back)
        addressdetails_back.setOnClickListener {
            onBackPressed()
        }
        linearOtpThree = findViewById(R.id.linearOtpThree)
        otpbackbtn = findViewById(R.id.otpbackbtn)
        otpbackbtn.setOnClickListener {
            onBackPressed()
        }


        pinView = findViewById(R.id.pinView)
        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                pinView.setLineColor(Color.parseColor("#E8F2FF"))
                linearOtpThree.visibility = View.INVISIBLE
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        mobile = intent.getStringExtra("EXTRA_MOBILE_NUMBER") ?: ""
        Log.d("Mobilenumberr", "Mobile Number: $mobile")

        if (mobile.isNotEmpty()) {
            val phoneText = "Enter the verification code we just sent on your number "
            val countryCode = "+91 "
            val phoneNumber: String = mobile

            val fullText = phoneText + countryCode + phoneNumber

            val spannableString = SpannableString(fullText)

            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.app_red)),
                phoneText.length,
                fullText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            otpNumberTv.text = spannableString
        } else {
            otpNumberTv.text = "Mobile number is missing"
        }

        otpBtn = findViewById(R.id.otpBtn)
        otpBtn.setOnClickListener {
            otp = pinView.text.toString()
            if (otp.isNotEmpty()) {
                verifyOtp(mobile, otp)
            } else {
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        pinView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (charSequence?.length == 6) {
                    otpBtn.isEnabled = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        otpBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.bluetext, null))
                        otpBtn.setTextColor(resources.getColor(R.color.hrmstext, null))
                    }

                    pinView.setLineColor(Color.parseColor("#E8F2FF"))
                    linearOtpThree.visibility = View.INVISIBLE
                    otpErrorTextView.visibility = View.GONE

                } else {
                    otpBtn.isEnabled = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        otpBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.hrmsbackgroundcolor, null))
                        otpBtn.setTextColor(resources.getColor(R.color.hrmstext, null))
                    }
                }
                if ( pinView.length() == 0)
                {
                    otpBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.bluetext, null))
                    otpBtn.setTextColor(resources.getColor(R.color.hrmstext, null))
                }
            }

            override fun afterTextChanged(editable: Editable?) {
                otpErrorTextView.visibility = View.GONE
            }
        })

        startCountdown()

        resendtimeOtpTv.setOnClickListener {
            pinView.text?.clear()
            resendCounter()
            loginData(mobile)
            timeOtpTv.visibility = View.VISIBLE
            resendtimeOtpTv.visibility = View.GONE
            otpErrorTextView.visibility = View.GONE
            otpBtn.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.hrmsbackgroundcolor, null))
            otpBtn.setTextColor(resources.getColor(R.color.hrmstext, null))
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
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Login response is empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    try {
                        val jObjError = JSONObject(response.errorBody()?.string() ?: "")
                        val errorMessage = jObjError.getString("message")
                        Log.e("ErrorMessage", errorMessage)
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(applicationContext, "Error parsing response", Toast.LENGTH_SHORT
                        ).show()
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
                }
            )

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

    private fun verifyOtp(mobile: String, otp: String) {
        showRefreshDialogue()

        val otpInt = otp.toIntOrNull() ?: run {
            Toast.makeText(this, "Invalid OTP format", Toast.LENGTH_SHORT).show()
            hideRefreshDialog()
            return
        }

        val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

        Log.e("mobile", "" + mobile)
        Log.e("otp", "" + otp)

        val verifyOtpBody = VerifyOtpBodyModel(phone = mobile, otpcode = otpInt)
        val verifyOtpCall = apiService.verifyOtp(verifyOtpBody)

        verifyOtpCall.enqueue(object : Callback<VerifyOtp> {
            override fun onResponse(
                call: retrofit2.Call<VerifyOtp>,
                response: retrofit2.Response<VerifyOtp>
            ) {
                hideRefreshDialog()
                if (response.isSuccessful) {
                    val verifyOtpResponse = response.body()
                    if (verifyOtpResponse != null) {
                        token = verifyOtpResponse.token.toString()
                        AccountUtils.saveAccessToken(applicationContext, token)
                        Log.e("tokennn", token)
                        val intent = Intent(applicationContext, HomeActivityViewController::class.java)
                        startActivity(intent)
                    } else {
                        showOtpError("Verification response is empty")
                    }
                } else {
                    showOtpError("Verification Failed, Please enter vaild otp")
                }
            }

            override fun onFailure(call: retrofit2.Call<VerifyOtp>, t: Throwable) {
                hideRefreshDialog()
                Log.e("VerifyOtpStatus", "Request failed: ${t.message ?: "Unknown error"}")
                showOtpError("Request failed: ${t.message}")
            }
        })
    }

    private fun showOtpError(errorMessage: String) {
        otpErrorTextView.text = errorMessage
        otpErrorTextView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun resendCounter() {
        Log.d("Debug", "startResendTimer() called")

        resendtimeOtpTv.isEnabled = false
        resendtimeOtpTv.visibility = View.GONE
        timeOtpTv.visibility = View.VISIBLE

        resendTimer = object : CountDownTimer(RESEND_TIMER_DURATION_MS, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timeOtpTv.text = "Send code again: %02d:%02d".format(
                    secondsRemaining / 60, secondsRemaining % 60
                ) + "s"
            }

            override fun onFinish() {
                resendtimeOtpTv.visibility = View.VISIBLE
                timeOtpTv.visibility = View.GONE
                resendtimeOtpTv.isEnabled = true
            }
        }.start()
    }

    fun startCountdown() {
        resendtimeOtpTv.visibility = View.GONE
        timeOtpTv.visibility = View.VISIBLE

        resendTimer = object : CountDownTimer(OTP_TIMER_DURATION_MS, 1000) {
            @SuppressLint("DefaultLocale")
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                timeOtpTv.text = "Send code again: %02d:%02d".format(
                    secondsRemaining / 60, secondsRemaining % 60
                ) + "s"
            }

            override fun onFinish() {
                resendtimeOtpTv.visibility = View.VISIBLE
                timeOtpTv.visibility = View.GONE
            }
        }.start()
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
