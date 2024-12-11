package com.xyug.hrms

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object AccountUtils {
    private const val EmpName = "EmpName"
    private const val FirstName = "FirstName"
    private const val LastName = "LastName"
    private const val CheckInTime = "Checkintime"
    private const val PREFS_NAME = "MyAppPrefs"
    private const val AccessToken = "token"
    private const val Employeeid = "Employeeid"
    private const val Orgid = "orgid"
    private const val CheckOutDate = "CheckOutDate"
    private const val Logindate = "Logindate"
    private const val Leavestartdate = "startdate"
    private const val Leaveenddate = "enddate"
    private const val Leavestardaytype = "startdaytype"
    private const val Leaveenddaytype = "enddaytype"
    private const val Startdatecount = "startdatecount"
    private const val Enddatecount = "senddatecount"
    private const val isPin = "pin"
    private const val CompoffLeavestartdate = "Compoffstartdate"
    private const val CompoffLeaveenddate = "Compoffenddate"
    private const val CompOffStartdatecount = "Compoffstartdatecount"
    private const val CompOffEnddatecount = "Compoffenddatecount"
    private const val Latitude = "Latitude"
    private const val Logitude = "Logitude"
    private const val ProfileImg = "ProfileImg"
    private const val OrganizationName = "OrganizationName"
    private const val LeaveType = "leavetype"
    private const val CompoffLeaveType = "compoffleavetype"
    private const val CompoffLeavestardaytype = "compoffstartdaytype"
    private const val CompoffLeaveenddaytype = "compoffenddaytype"
    private const val ischeckin = "ischeckin"
    private const val Checkinstatusname = "Checkinstatusname"
    private const val EarlyLeavedate = "earlyleavedate"
    private const val Department = "department"
    private const val Designatation = "desigmatation"
    private const val Password = "password"
    private const val CustomerID = "CustomerID"
    private const val Email = "Email"
    private const val Name = "Name"
    private const val Mobile = "phone"
    private const val existance = "existance"
    private const val Operator = "operator"
    private const val Phone = "phone"
    private const val KEY_IMAGE_URL = "image_url"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    fun saveImageUrl(context: Context, imageUrl: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_IMAGE_URL, imageUrl)
        editor.apply()
    }

    fun getImageUrl(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_IMAGE_URL, null)
    }
    fun setischeckin(context: Context, value: Boolean) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putBoolean(ischeckin, value).apply()
    }

    fun getischeckin(context: Context): Boolean {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getBoolean(ischeckin, false)
    }

    fun setEmpName(context: Context, EpName: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(EmpName, EpName).apply()
    }

    fun getEmpName(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(EmpName, "") ?: ""
    }

    fun setCheckintime(context: Context, value: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CheckInTime, value).apply()
    }

    fun getCheckintime(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CheckInTime, "") ?: ""
    }

    fun saveAccessToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(AccessToken, token)
        editor.apply()
    }

    fun getAccessToken(context: Context): String {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(AccessToken, "") ?: ""
    }

    fun clearAccessToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove(AccessToken)
        editor.apply()
    }
    fun setEmployeeid(context: Context, empid: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Employeeid, empid).apply()
    }

    fun getEmployeeid(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Employeeid, "") ?: ""
    }

    fun setLogindate(context: Context, customerid: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Logindate, customerid).apply()
    }

    fun getLogindate(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Logindate, "") ?: ""
    }

    fun setOrgid(context: Context, orgid: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Orgid, orgid).apply()
    }

    fun getOrgid(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Orgid, "") ?: ""
    }

    fun setEmail(context: Context, email: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Email, email).apply()
    }

    fun getEmail(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Email, "") ?: ""
    }

    fun setLeavestartdate(context: Context, leavstardate: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Leavestartdate, leavstardate).apply()
    }

    fun getLeavestartdate(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Leavestartdate, "") ?: ""
    }

    fun setLeaveenddate(context: Context, leavenddate: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Leaveenddate, leavenddate).apply()
    }

    fun getLeaveenddate(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Leaveenddate, "") ?: ""
    }

    fun setLeavestardaytype(context: Context, startdatetype: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Leavestardaytype, startdatetype).apply()
    }

    fun getLeavestardaytype(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Leavestardaytype, "") ?: ""
    }

    fun setLeaveenddaytype(context: Context, enddatetype: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Leaveenddaytype, enddatetype).apply()
    }

    fun getLeaveenddaytype(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Leaveenddaytype, "") ?: ""
    }

    fun setStartdatecount(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Startdatecount, startdaycont).apply()
    }

    fun getStartdatecount(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Startdatecount, "") ?: ""
    }

    fun setEnddatecount(context: Context, enddaycount: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Enddatecount, enddaycount).apply()
    }

    fun getEnddatecount(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Enddatecount, "") ?: ""
    }

    fun setCompoffLeavestardaytype(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CompoffLeavestardaytype, startdaycont).apply()
    }

    fun getCompoffLeavestardaytype(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CompoffLeavestardaytype, "") ?: ""
    }

    fun setCompoffLeaveenddaytype(context: Context, enddaycount: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CompoffLeaveenddaytype, enddaycount).apply()
    }

    fun getCompoffLeaveenddaytype(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CompoffLeaveenddaytype, "") ?: ""
    }

    fun setIsPin(context: Context, value: Boolean) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putBoolean(isPin, value).apply()
    }

    fun getIsPin(context: Context): Boolean {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getBoolean(isPin, true)
    }

    fun setLatitude(context: Context, latitude: Double) {
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("latitude", latitude.toString())
        editor.apply()
    }

    fun getLatitude(context: Context): String {
        val sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("latitude", "") ?: ""
    }

    fun setLongitude(context: Context, longitude: Double) {
        val sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("longitude", longitude.toString())
        editor.apply()
    }

    fun getLongitude(context: Context): String {
        val sharedPref = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("longitude", "") ?: ""
    }


    fun setProfileImg(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(ProfileImg, startdaycont).apply()
    }

    fun getProfileImg(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(ProfileImg, "") ?: ""
    }

    fun setOrganizationName(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(OrganizationName, startdaycont).apply()
    }

    fun getOrganizationName(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(OrganizationName, "") ?: ""
    }

    fun setLeaveType(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(LeaveType, startdaycont).apply()
    }

    fun getLeaveType(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(LeaveType, "") ?: ""
    }

    fun setCompoffLeaveType(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CompoffLeaveType, startdaycont).apply()
    }

    fun getCompoffLeaveType(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CompoffLeaveType, "") ?: ""
    }

    fun setCheckOutDate(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CheckOutDate, startdaycont).apply()
    }

    fun getCheckOutDate(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CheckOutDate, "") ?: ""
    }

    fun setEarlyLeavedate(context: Context, startdaycont: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(EarlyLeavedate, startdaycont).apply()
    }

    fun getEarlyLeavedate(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(EarlyLeavedate, "") ?: ""
    }

    fun setFirstName(context: Context, firstname: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(FirstName, firstname).apply()
    }

    fun getFirstName(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(FirstName, "") ?: ""
    }

    fun setLastName(context: Context, lastname: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(LastName, lastname).apply()
    }

    fun getLastName(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(LastName, "") ?: ""
    }

    fun setDepartment(context: Context, department: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Department, department).apply()
    }

    fun getDepartment(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Department, "") ?: ""
    }

    fun setDesignation(context: Context, designation: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Designatation, designation).apply()
    }

    fun getDesignation(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Designatation, "") ?: ""
    }

    fun setOperator(context: Context, operator: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Operator, operator).apply()
    }

    fun getOperator(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Operator, "") ?: ""
    }

    fun setPhone(context: Context, phone: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Phone, phone).apply()
    }

    fun getPhone(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Phone, "") ?: ""
    }

    fun setCustomerID(context: Context, customerid: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(CustomerID, customerid).apply()
    }

    fun getCustomerID(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(CustomerID, "") ?: ""
    }

    fun setName(context: Context, name: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Name, name).apply()
    }

    fun getName(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Name, "") ?: ""
    }

    fun setMobile(context: Context, mobile: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Mobile, mobile).apply()
    }

    fun getMobile(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Mobile, "") ?: ""
    }

    fun setExistance(context: Context, mobile: Boolean) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putBoolean(existance, mobile).apply()
    }

    fun getExistance(context: Context): Boolean {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getBoolean(existance, false)
    }

    fun setCheckinstatusname(context: Context, checkinstatusname: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Checkinstatusname, checkinstatusname).apply()
    }

    fun getCheckinstatusname(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Checkinstatusname, "") ?: ""
    }

    fun setPassword(context: Context, password: String) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().putString(Password, password).apply()
    }

    fun getPassword(context: Context): String {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(Password, "") ?: ""
    }

    fun clearall(context: Context) {
        val sharedPref = getSharedPreferences(context)
        sharedPref.edit().clear().apply()
    }
}
