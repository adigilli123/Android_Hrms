package com.example.Leaves

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Rect
import android.os.Bundle
import android.os.Parcel
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.Interface.ApplyleaveBodyModel
import com.example.Interface.ApplyleaveModel
import com.example.Interface.LeavetypeModel
import com.example.Interface.NotifyEmployee1
import com.example.Interface.ServerApisInterface
import com.example.hrms_xyug.AddEducationActivity
import com.example.hrms_xyug.HomeActivityViewController
import com.example.hrms_xyug.R
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.xyug.hrms.AccountUtils
import com.xyug.hrms.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ApplyLeave : AppCompatActivity() {
    private lateinit var daytype1_Spinner: Spinner
    private lateinit var daytype2_Spinner: Spinner
    private lateinit var fromdate_Tv: TextView
    private lateinit var notifyEmpBtn: TextView
    private lateinit var todate_Tv: TextView
    private lateinit var leavecount: TextView
    private lateinit var daytype_Tvv: TextView
    private lateinit var todate_Ln: LinearLayout
    private lateinit var applyleave_back: ImageView
    private lateinit var add_document: ImageView
    private lateinit var datetype_Rl: RelativeLayout
    private lateinit var radioCasual: RadioButton
    private lateinit var radioMedical: RadioButton
    private lateinit var radioOther: RadioButton
    private lateinit var documentlist_Tv: TextView
    private lateinit var extra_textview4: TextView
    private lateinit var leavereason_Et: EditText
    private lateinit var documentbox1: RelativeLayout
    private lateinit var submitBtn: Button
    private var totalDays: Int = 0
    private var dayType1Fraction = 1.0
    private var dayType2Fraction = 1.0
    private var previousStartDate: Long? = null
    private var previousEndDate: Long? = null
    private var selectedLeaveTypeIdGlobal: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apply_leave)

        extra_textview4 = findViewById(R.id.extra_textview4)
        documentlist_Tv = findViewById(R.id.documentlist_Tv)
        documentbox1 = findViewById(R.id.documentbox1)
        radioCasual = findViewById(R.id.radioCasual)
        radioMedical = findViewById(R.id.radioMedical)
        radioOther = findViewById(R.id.radioOther)
        submitBtn = findViewById(R.id.submitBtn)
        leavereason_Et = findViewById(R.id.leavereason_Et)

        documentlist_Tv.visibility = View.GONE
        documentbox1.visibility = View.GONE

        radioMedical.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                documentlist_Tv.visibility = View.VISIBLE
                documentbox1.visibility = View.VISIBLE
            } else {
                documentlist_Tv.visibility = View.GONE
                documentbox1.visibility = View.GONE
            }
        }


        val leaveType = intent.getStringExtra("leaveType")
        when (leaveType) {
            "casual" -> radioCasual.isChecked = true
            "sick" -> radioMedical.isChecked = true
            "other" -> radioOther.isChecked = true
        }

        add_document = findViewById(R.id.add_document)
        add_document.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, AddEducationActivity.REQUEST_CODE_PICK_IMAGE)
        }

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

        notifyEmpBtn = findViewById(R.id.notifyEmpBtn)
        notifyEmpBtn.setOnClickListener {
            val intent = Intent(this, NotifyEmpActivity::class.java)
            startActivity(intent)
        }

        daytype1_Spinner = findViewById(R.id.daytype1_Spinner)
        daytype2_Spinner = findViewById(R.id.daytype2_Spinner)
        datetype_Rl = findViewById(R.id.datetype_Rl)
        fromdate_Tv = findViewById(R.id.fromdate_Tv)
        todate_Tv = findViewById(R.id.todate_Tv)
        todate_Ln = findViewById(R.id.todate_Ln)
        leavecount = findViewById(R.id.leavecount)
        daytype_Tvv = findViewById(R.id.daytype_Tvv)
        applyleave_back = findViewById(R.id.applyleave_back)
        getLeavetype()

        daytype1_Spinner.setPopupBackgroundResource(R.drawable.spinner_dropdown_icon)
        daytype2_Spinner.setPopupBackgroundResource(R.drawable.spinner_dropdown_icon)
        daytype1_Spinner.isEnabled = false

        applyleave_back.setOnClickListener {
            navigateToProfileFragment()
        }

        fromdate_Tv.setOnClickListener {
            showDatePickerDialog()
        }

        todate_Tv.setOnClickListener {
            showDatePickerDialog()
        }

        val dayTypes = arrayOf("Select Type", "Full Day", "First Half", "Second Half")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, dayTypes)
        adapter.setDropDownViewResource(R.layout.spinner_item1)
        daytype1_Spinner.adapter = adapter

        val dayTypes1 = arrayOf("Select Type", "Full Day", "First Half", "Second Half")
        val adapter1 = ArrayAdapter(this, R.layout.spinner_item1, dayTypes1)
        adapter1.setDropDownViewResource(R.layout.spinner_item1)
        daytype2_Spinner.adapter = adapter1

        setupSpinnerListeners()
        val employeeNumber = intent.getStringExtra("EMPLOYEE_NUMBER")

//        if (employeeNumber != null) {
//            Log.d("ApplyLeaveActivity", "Employee Number: $employeeNumber")
//          //  applyLeave(employeeNumber.toString())
//        } else {
//            Log.e("ApplyLeaveActivity", "Employee Number not passed")
//        }

        submitBtn.setOnClickListener {
            val employeeNumber = employeeNumber?.toString()
            if (employeeNumber != null) {
                applyLeave(employeeNumber)
            } else {
                Toast.makeText(this, "Employee number is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSpinnerListeners() {
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateLeaveCount()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        daytype1_Spinner.onItemSelectedListener = spinnerListener
        daytype2_Spinner.onItemSelectedListener = spinnerListener
    }

    private fun updateLeaveCount() {
        var adjustedLeaveCount = totalDays.toDouble()

        if (totalDays == 1) {
            when (daytype1_Spinner.selectedItem) {
                "First Half", "Second Half" -> adjustedLeaveCount = 0.5
                "Full Day" -> adjustedLeaveCount = 1.0
                else -> adjustedLeaveCount = 1.0
            }
        } else {
            if (daytype1_Spinner.selectedItem == "First Half" || daytype1_Spinner.selectedItem == "Second Half") {
                adjustedLeaveCount -= 0.5
            }

            if (daytype2_Spinner.selectedItem == "First Half" || daytype2_Spinner.selectedItem == "Second Half") {
                adjustedLeaveCount -= 0.5
            }
        }

        if (adjustedLeaveCount < 0) {
            adjustedLeaveCount = 0.0
        }

        leavecount.text = adjustedLeaveCount.toString()
    }

    private fun showDatePickerDialog() {
        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val dateValidator = object : CalendarConstraints.DateValidator {
            override fun isValid(date: Long): Boolean {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date

                if (date < today) {
                    return false
                }

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    return false
                }

                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && isSecondOrFourthSaturday(
                        calendar
                    )
                ) {
                    return false
                }
                return !isNationalHoliday(calendar)
            }

            override fun describeContents(): Int = 0
            override fun writeToParcel(p0: Parcel, flags: Int) {}
        }

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(dateValidator)
            .build()

        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setTitleText("Select a date range")
        builder.setCalendarConstraints(constraintsBuilder)
        builder.setTheme(R.style.CustomDatePickerTheme)

        val datePicker = builder.build()

        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val startDateString = startDate?.let { sdf.format(Date(it)) } ?: ""
            val endDateString = endDate?.let { sdf.format(Date(it)) } ?: ""

            fromdate_Tv.text = startDateString
            todate_Tv.text = endDateString

            if (startDate != null && endDate != null && startDate == endDate) {
                datetype_Rl.visibility = View.GONE
                todate_Ln.visibility = View.GONE
                daytype_Tvv.visibility = View.GONE
                daytype1_Spinner.isEnabled = true
                daytype2_Spinner.isEnabled = false
                totalDays = 1

                val dayTypes = arrayOf("Select Type", "Full Day", "First Half", "Second Half")
                val adapter = ArrayAdapter(this, R.layout.spinner_item, dayTypes)
                adapter.setDropDownViewResource(R.layout.spinner_item)
                daytype1_Spinner.adapter = adapter
                daytype1_Spinner.setSelection(0)

            } else {
                datetype_Rl.visibility = View.VISIBLE
                todate_Ln.visibility = View.VISIBLE
                daytype_Tvv.visibility = View.VISIBLE
                daytype1_Spinner.isEnabled = true
                daytype2_Spinner.isEnabled = true

                totalDays = 0
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = startDate!!

                while (calendar.timeInMillis <= endDate!!) {
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
                    if (dayOfWeek != Calendar.SUNDAY && !(dayOfWeek == Calendar.SATURDAY && isSecondOrFourthSaturday(
                            calendar
                        )) && !isNationalHoliday(calendar)
                    ) {
                        totalDays++
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }

                val dayTypes = arrayOf("Select Type", "Full Day", "First Half", "Second Half")
                val adapter = ArrayAdapter(this, R.layout.spinner_item, dayTypes)
                adapter.setDropDownViewResource(R.layout.spinner_item)
                daytype1_Spinner.adapter = adapter
                daytype1_Spinner.setSelection(0)

                val dayTypes1 = arrayOf("Select Type", "Full Day", "First Half", "Second Half")
                val adapter1 = ArrayAdapter(this, R.layout.spinner_item1, dayTypes1)
                adapter1.setDropDownViewResource(R.layout.spinner_item1)
                daytype2_Spinner.adapter = adapter1
                daytype2_Spinner.setSelection(0)
            }

            updateLeaveCount()
        }
        datePicker.show(supportFragmentManager, "DATE_PICKER")
    }

    private fun isNationalHoliday(calendar: Calendar): Boolean {
        val year = calendar.get(Calendar.YEAR)
        val holidays = listOf(
            Calendar.getInstance().apply { set(year, Calendar.JANUARY, 26) },
            Calendar.getInstance().apply { set(year, Calendar.AUGUST, 15) },
            Calendar.getInstance().apply { set(year, Calendar.OCTOBER, 2) },
            Calendar.getInstance().apply { set(year, Calendar.JANUARY, 1) },
            Calendar.getInstance().apply { set(year, Calendar.DECEMBER, 25) },
            Calendar.getInstance().apply { set(year, Calendar.MAY, 1) },
            Calendar.getInstance().apply { set(year, Calendar.MARCH, 8) },
            Calendar.getInstance().apply { set(year, Calendar.NOVEMBER, 12) },
            Calendar.getInstance().apply { set(year, Calendar.APRIL, 22) })

        return holidays.any { holiday ->
            calendar.get(Calendar.MONTH) == holiday.get(Calendar.MONTH) && calendar.get(Calendar.DAY_OF_MONTH) == holiday.get(Calendar.DAY_OF_MONTH)
        }
    }

    private fun isSecondOrFourthSaturday(calendar: Calendar): Boolean {
        val weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH)
        return weekOfMonth == 2 || weekOfMonth == 4
    }


    private fun navigateToProfileFragment() {
        val intent = Intent(this, HomeActivityViewController::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)
        finish()
    }

    private fun getLeavetype() {
        val token = AccountUtils.getAccessToken(applicationContext)
        val apiService: ServerApisInterface =
            RetrofitClient.get().create(ServerApisInterface::class.java)
        Log.d("Token", "Token: $token")

        val call: Call<LeavetypeModel> = apiService.getLeavetype(token)
        call.enqueue(object : Callback<LeavetypeModel> {
            override fun onResponse(
                call: Call<LeavetypeModel>,
                response: Response<LeavetypeModel>) {
                if (response.isSuccessful) {
                    val leaveTypes = response.body()?.data ?: emptyList()

                    val radioGroup = findViewById<RadioGroup>(R.id.leaveTypeRadioGroup)
                    radioGroup.removeAllViews()

                    leaveTypes.forEach { leaveType ->
                        val radioButton = RadioButton(applicationContext).apply {
                            text = leaveType.leave_type
                            textSize = 14f
                            setTextColor(ContextCompat.getColor(applicationContext, R.color.hrmstext)
                            )
                            setTypeface(ResourcesCompat.getFont(applicationContext, R.font.mulish_medium))
                            buttonTintList = ColorStateList.valueOf(ContextCompat.getColor(applicationContext, R.color.deepSky))
                            tag = leaveType._id
                        }
                        radioGroup.addView(radioButton)
                    }

                    radioGroup.setOnCheckedChangeListener { group, checkedId ->
                        val selectedRadioButton = group.findViewById<RadioButton>(checkedId)
                        val selectedLeaveTypeId = selectedRadioButton?.tag as? String
                        Log.d("ApplyLeaveActivity", "Selected Leave Type ID: $selectedLeaveTypeId")
                        selectedLeaveTypeId?.let {
                             selectedLeaveTypeIdGlobal = it
                        }
                    }
                } else {
                    Toast.makeText(this@ApplyLeave, "Failed to fetch leave types", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LeavetypeModel>, t: Throwable) {
                Log.e("Leavetype", "Failed to fetch leave types: ${t.message}")
                Toast.makeText(
                    this@ApplyLeave,
                    "Connection Error: Unable to reach the server",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }


    private fun applyLeave(employeeNumber: String?) {
        val fromDayType = daytype1_Spinner.selectedItem.toString()
        val toDayType = daytype2_Spinner.selectedItem.toString()
        val fromDate = fromdate_Tv.text.toString()
        val toDate = todate_Tv.text.toString()
        val leaveReason = leavereason_Et.text.toString()
        val leaveCount = leavecount.text.toString().toDoubleOrNull() ?: 0.0

        if (employeeNumber.isNullOrEmpty()) {
            Toast.makeText(this, "Employee number is missing", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedLeaveTypeIdGlobal == null) {
            Toast.makeText(this, "Please select a leave type", Toast.LENGTH_SHORT).show()
            return
        }

        val empNumber = employeeNumber.toIntOrNull()
        if (empNumber == null) {
            Toast.makeText(this, "Invalid employee number format", Toast.LENGTH_SHORT).show()
            return
        }

        val token = AccountUtils.getAccessToken(applicationContext)
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Access token is missing or invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (fromDayType.isEmpty() || toDayType.isEmpty() || fromDate.isEmpty() || toDate.isEmpty() || leaveReason.isEmpty()) {
            Toast.makeText(this, "Please fill in all the required fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val applyleavebodymodel = ApplyleaveBodyModel(
            fromDayType = fromDayType,
            toDayType = toDayType,
            fromDate = fromDate,
            toDate = toDate,
            leaveType = selectedLeaveTypeIdGlobal!!,
            leavesCount = leaveCount,
            reasonLeave = leaveReason,
            notifyEmployees = arrayListOf(NotifyEmployee1(employeeNumber = empNumber)))

        val apiService: ServerApisInterface =  RetrofitClient.get().create(ServerApisInterface::class.java)

        val call: Call<ApplyleaveModel> = apiService.applyleave(token, applyleavebodymodel)
        call.enqueue(object : Callback<ApplyleaveModel> {
            override fun onResponse(
                call: Call<ApplyleaveModel>,
                response: Response<ApplyleaveModel>) {
                if (response.isSuccessful) {
                    val leaveResponse = response.body()
                    Toast.makeText(applicationContext, "Leave applied successfully: ${leaveResponse?.message}", Toast.LENGTH_SHORT).show()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error"
                    Toast.makeText(applicationContext, "Failed to apply leave: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ApplyleaveModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
