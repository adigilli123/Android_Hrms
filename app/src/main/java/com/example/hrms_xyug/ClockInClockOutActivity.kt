package com.example.hrms_xyug
 import android.app.AlarmManager
 import android.app.PendingIntent
 import android.content.BroadcastReceiver
 import android.content.Context
 import android.content.Intent
 import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Adapters.AttendanceHistoryAdapter
import com.example.Interface.AttendanceHistoryModel
import com.example.Interface.ClockInBodyModel
import com.example.Interface.ClockInModel
import com.example.Interface.ClockOutModel
import com.example.Interface.ServerApisInterface
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
 import com.google.gson.Gson
 import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
 import okhttp3.ResponseBody
 import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
 class ClockInClockOutActivity : AppCompatActivity() {
    private lateinit var clockSwitch: Switch
    private lateinit var statusTextView: TextView
    private lateinit var clockText2: TextView
    private lateinit var clockText: TextView
    private lateinit var clockText3: TextView
    private lateinit var clockText4: TextView
    private lateinit var holiday: ImageView
    private lateinit var allclockin_back: ImageView
    private lateinit var cloclkktext: RelativeLayout
    private lateinit var jhbdvasfjabsdjv: RelativeLayout
    private lateinit var ksbdkjjab: RelativeLayout
    private lateinit var attendancehistoryRecycler: RecyclerView
    private lateinit var labeledSwitch: Switch
    data class ErrorResponse(val message: String)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var isUserAction = true
      companion object {
        const val PERMISSION_REQUEST_CODE = 100
        const val PREFS_NAME = "AttendancePrefs"
        const val IS_CLOCKED_IN_KEY = "isClockedIn"
    }

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock_in_clock_out)
         setClockOutAlarm(applicationContext)


         ksbdkjjab=findViewById(R.id.ksbdkjjab)
        jhbdvasfjabsdjv=findViewById(R.id.jhbdvasfjabsdjv)
        holiday=findViewById(R.id.holiday)
         allclockin_back=findViewById(R.id.allclockin_back)
         allclockin_back.setOnClickListener {
             onBackPressed()
         }
        labeledSwitch = findViewById(R.id.labeledSwitch)
        clockText2 = findViewById(R.id.clockText2)
        clockText = findViewById(R.id.clockText)
        cloclkktext = findViewById(R.id.cloclkktext)
        clockText3 = findViewById(R.id.clockText3)
        clockText4 = findViewById(R.id.clockText4)
        attendancehistoryRecycler = findViewById(R.id.attendancehistoryRecycler)
        getAttendanceHistory()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
         val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      //  val isClockedIn = sharedPreferences.getBoolean(IS_CLOCKED_IN_KEY, false)
      //  labeledSwitch.isChecked = isClockedIn
         val isClockedIn = getClockInState()
         isUserAction = false
         labeledSwitch.isChecked = isClockedIn
         updateClockStateUI(isClockedIn)
         isUserAction = true
      //  updateClockInStatus(isClockedIn)
         val calendar: Calendar = Calendar.getInstance()
         val dayOfWeek: Int = calendar.get(Calendar.DAY_OF_WEEK)
         val weekOfMonth: Int = calendar.get(Calendar.WEEK_OF_MONTH)
         val isSunday = dayOfWeek == Calendar.SUNDAY
         val isSaturday = dayOfWeek == Calendar.SATURDAY
         val isSecondSaturday = isSaturday && weekOfMonth == 2
         val isFourthSaturday = isSaturday && weekOfMonth == 4

         val isPublicHoliday: Boolean = isPublicHoliday(calendar)


         labeledSwitch.setOnCheckedChangeListener { _, isChecked ->
             if (isUserAction) {
                 val message = if (isChecked) {
                     "Are you sure you want to Clock In?"
                 } else {
                     "Are you sure you want to Clock Out?"
                 }

                 val dialog = AlertDialog.Builder(this)
                     .setTitle("Confirmation")
                     .setMessage(message)
                     .setPositiveButton("Yes") { dialog, _ ->
                         if (isChecked) {
                             clockInEmployee()
                         } else {
                             clockOutApi()
                         }
                         saveClockInState(isChecked)
                         updateClockStateUI(isChecked)
                         Log.d("MainActivity", "Saved Clock State: $isChecked")
                         dialog.dismiss()
                     }
                     .setNegativeButton("No") { dialog, _ ->
                         isUserAction = false
                         labeledSwitch.isChecked = !isChecked
                         isUserAction = true
                         dialog.dismiss()
                     }
                     .create()

                 dialog.setCancelable(false)
                 dialog.setCanceledOnTouchOutside(false)
                 dialog.show()
             }
         }
     }

     class ClockOutReceiver : BroadcastReceiver() {
         override fun onReceive(context: Context, intent: Intent) {
             val activityIntent = Intent(context, MainActivity::class.java)
             activityIntent.putExtra("autoClockOut", true)
             context.startService(activityIntent)
         }
     }

     fun setClockOutAlarm(context: Context) {
         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
         val intent = Intent(context, ClockOutReceiver::class.java)

         val calendar = Calendar.getInstance()
         calendar.set(Calendar.HOUR_OF_DAY, 0)
         calendar.set(Calendar.MINUTE, 0)
         calendar.set(Calendar.SECOND, 0)
         calendar.set(Calendar.MILLISECOND, 0)

         if (Calendar.getInstance().after(calendar)) {
             calendar.add(Calendar.DAY_OF_YEAR, 1)
         }

         val pendingIntent = PendingIntent.getBroadcast(
             context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )

         alarmManager.setRepeating(
             AlarmManager.RTC_WAKEUP,
             calendar.timeInMillis,
             AlarmManager.INTERVAL_DAY, // Repeat daily
             pendingIntent
         )
     }

     private fun isPublicHoliday(calendar: Calendar): Boolean {
         val day = calendar[Calendar.DAY_OF_MONTH]
         val month = calendar[Calendar.MONTH]
         //  ksbdkjjab.setBackgroundColor(ContextCompat.getColor(this, R.color.icongrey))
         return when {

             month == Calendar.JANUARY && day == 26 -> true   // Republic Day
             month == Calendar.MARCH && day == 25 -> true     // Holi (2024 date)
             month == Calendar.APRIL && day == 10 -> true     // Eid ul-Fitr (2024 date, may vary based on moon sighting)
             month == Calendar.APRIL && day == 14 -> true     // Dr. Ambedkar Jayanti
             month == Calendar.APRIL && day == 17 -> true     // Ram Navami (2024 date)
             month == Calendar.APRIL && day == 21 -> true     // Mahavir Jayanti (2024 date)
             month == Calendar.MAY && day == 1 -> true        // Labour Day
             month == Calendar.MAY && day == 23 -> true       // Buddha Purnima (2024 date)
             month == Calendar.AUGUST && day == 15 -> true    // Independence Day
             month == Calendar.AUGUST && day == 28 -> true    // Janmashtami (2024 date)
             month == Calendar.SEPTEMBER && day == 17 -> true // Ganesh Chaturthi (2024 date)
             month == Calendar.OCTOBER && day == 2 -> true    // Gandhi Jayanti
             month == Calendar.OCTOBER && day == 11 -> true   // Dussehra (2024 date)
             month == Calendar.OCTOBER && day == 31 -> true   // Diwali (2024 date)
             month == Calendar.NOVEMBER && day == 1 -> true   // Govardhan Puja (2024 date)
             month == Calendar.NOVEMBER && day == 15 -> true  // Guru Nanak Jayanti (2024 date)
             month == Calendar.DECEMBER && day == 25 -> true  // Christmas
             isEidAlAdha(calendar) -> true                   // Eid al-Adha (Bakrid) - dynamic
             else -> false
         }
     }

     private fun isEidAlAdha(calendar: Calendar): Boolean {
         // ksbdkjjab.setBackgroundColor(ContextCompat.getColor(this, R.color.icongrey))
         return calendar[Calendar.MONTH] == Calendar.JUNE && calendar[Calendar.DAY_OF_MONTH] == 17
     }

     fun handleApiError(errorBody: ResponseBody?): String {
         return try {
             // If the errorBody is not null, parse it to extract the message
             val errorResponse = Gson().fromJson(errorBody?.string(), ErrorResponse::class.java)
             errorResponse.message
         } catch (e: Exception) {
             "An unexpected error occurred"
         }
     }

     private fun clockInEmployee() {
         val token = AccountUtils.getAccessToken(applicationContext)
         val latitude = AccountUtils.getLatitude(applicationContext)
         val longitude = AccountUtils.getLongitude(applicationContext)
         val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)
         val clockInBody = ClockInBodyModel(lat = latitude, lon = longitude)

         val call: Call<ClockInModel> = apiService.postClockIn(token, clockInBody)
         call.enqueue(object : Callback<ClockInModel> {
             override fun onResponse(call: Call<ClockInModel>, response: Response<ClockInModel>) {
                 if (response.isSuccessful) {
                     saveClockInState(true)
                     updateClockStateUI(true)

                     val clockInModel = response.body()
                     val clockInTime = clockInModel?.datasaved?.clockInTime
                     val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                     val formattedTime = clockInTime?.let { dateFormat.format(it) }
                     clockText3.text = "You clocked in at: ${formattedTime ?: "Time not available"}"
                     Toast.makeText(applicationContext, "Clock in successful", Toast.LENGTH_SHORT).show()
                     getAttendanceHistory()
                     Log.d("ClockIn", "Clock in successful: ${clockInModel?.message}")
                 } else {
                     resetSwitchStateIn()
                     saveClockInState(false)
                     updateClockStateUI(false)

//                     val errorMessage = response.errorBody()?.string()
                     val errorMessage = handleApiError(response.errorBody())
                     Toast.makeText(applicationContext, "$errorMessage", Toast.LENGTH_SHORT).show()
                     Log.e("ClockIn", "Error: $errorMessage")
                 }
                 labeledSwitch.isEnabled = true
             }

             override fun onFailure(call: Call<ClockInModel>, t: Throwable) {
                 resetSwitchStateIn()
                 saveClockInState(false)
                 updateClockStateUI(false)

                 Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                 Log.e("ClockIn", "Failed to clock in: ${t.message}")
                 labeledSwitch.isEnabled = true
             }
         })
     }

     private fun resetSwitchStateIn() {
         isUserAction = false
         labeledSwitch.isChecked = false
         isUserAction = true
     }

     private fun clockOutApi() {
         val token = AccountUtils.getAccessToken(applicationContext)
         val apiService: ServerApisInterface = RetrofitClient.get().create(ServerApisInterface::class.java)

         val call: Call<ClockOutModel> = apiService.postClockOut(token)
         call.enqueue(object : Callback<ClockOutModel> {
             override fun onResponse(call: Call<ClockOutModel>, response: Response<ClockOutModel>) {
                 if (response.isSuccessful) {
                     saveClockInState(false)
                     updateClockStateUI(false)

                     val clockOutModel = response.body()
                     val clockOutTime = clockOutModel?.attendance?.clockOutTime
                     val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                     val formattedTime = clockOutTime?.let { dateFormat.format(it) }
                     clockText3.text = "You clocked out at: ${formattedTime ?: "Time not available"}"

                     Toast.makeText(applicationContext, "Clock out successful", Toast.LENGTH_SHORT).show()
                     getAttendanceHistory()
                     Log.d("ClockOut", "Clock out successful: ${clockOutModel?.message}")
                 } else {
                     resetSwitchStateOut()
                     saveClockInState(true)
                     updateClockStateUI(true)

                     val errorMessage = handleApiError(response.errorBody())
                     Toast.makeText(applicationContext, "$errorMessage", Toast.LENGTH_SHORT).show()
                     Log.e("ClockOut", "Error: $errorMessage")
                 }
                 labeledSwitch.isEnabled = true
             }

             override fun onFailure(call: Call<ClockOutModel>, t: Throwable) {
                 resetSwitchStateOut()
                 saveClockInState(true)
                 updateClockStateUI(true)


                 Toast.makeText(applicationContext, "${t.message}", Toast.LENGTH_SHORT).show()
                 Log.e("ClockOut", "Failed to clock out: ${t.message}")
                 labeledSwitch.isEnabled = true
             }
         })
     }

     override fun onStart() {
         super.onStart()
         if (intent?.getBooleanExtra("autoClockOut", false) == true) {
             clockOutApi()
         }
     }

     private fun resetSwitchStateOut() {
         isUserAction = false
         labeledSwitch.isChecked = true
         isUserAction = true
     }

     private fun saveClockInState(isClockedIn: Boolean) {
         val sharedPreferences = getSharedPreferences("ClockStatePrefs", Context.MODE_PRIVATE)
         val editor = sharedPreferences.edit()
         editor.putBoolean("IS_CLOCKED_IN", isClockedIn)
         editor.apply()
     }

     private fun getClockInState(): Boolean {
         val sharedPreferences = getSharedPreferences("ClockStatePrefs", Context.MODE_PRIVATE)
         return sharedPreferences.getBoolean("IS_CLOCKED_IN", false)
     }

     private fun updateClockStateUI(isClockedIn: Boolean) {
         labeledSwitch.isChecked = isClockedIn
         clockText3.text = if (isClockedIn) {
             "You are Clocked In"
         } else {
             "You are Clocked Out"
         }
     }

     fun getAttendanceHistory() {
         val token = AccountUtils.getAccessToken(applicationContext)
         val apiService: ServerApisInterface =
             RetrofitClient.get().create(ServerApisInterface::class.java)
         Log.d("Token", "Token: $token")

         val call: Call<AttendanceHistoryModel> = apiService.getAttendenceHistory(token)
         call.enqueue(object : Callback<AttendanceHistoryModel> {
             override fun onResponse(call: Call<AttendanceHistoryModel>, response: Response<AttendanceHistoryModel>) {
                 if (response.isSuccessful) {
                     val attendanceHistoryModel = response.body()
                     val attendanceList = attendanceHistoryModel?.attendance.orEmpty()

                     if (attendanceList.isEmpty()) {
                         if (attendanceList.isEmpty()) {
                             if (labeledSwitch.isChecked) {
                                 // If the switch is in the "Clock Out" position, automatically switch to "Clock In"
                                 Toast.makeText(this@ClockInClockOutActivity, "Attendance history is empty. Automatically switching to Clock In.", Toast.LENGTH_SHORT).show()

                                 // Set switch to "Clock In" position without triggering the listener
                                 isUserAction = false
                                 labeledSwitch.isChecked = false  // Switch to Clock In position (unchecked)
                                 isUserAction = true
                             }
                             // If the switch is already in "Clock In", do nothing
                         } else {
                             // Display the attendance history in the RecyclerView
                             attendancehistoryRecycler.apply {
                                 layoutManager = LinearLayoutManager(applicationContext)
                                 adapter = AttendanceHistoryAdapter(attendanceList)
                             }
                         }
                         // If the switch is already in "Clock In", no action is needed
                     } else {
                         // Display the attendance history in the RecyclerView
                         attendancehistoryRecycler.apply {
                             layoutManager = LinearLayoutManager(applicationContext)
                             adapter = AttendanceHistoryAdapter(attendanceList)
                         }
                     }
                 } else {
                     Toast.makeText(this@ClockInClockOutActivity, "Failed to fetch attendance history", Toast.LENGTH_SHORT).show()
                 }
             }

             override fun onFailure(call: Call<AttendanceHistoryModel>, t: Throwable) {
                 Log.e("AttendanceHistory", "Failed to fetch attendance history: ${t.message}")
             }
         })
     }
 }
