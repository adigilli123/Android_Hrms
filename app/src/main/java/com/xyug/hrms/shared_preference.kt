package com.xyug.hrms

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("HRMS", Context.MODE_PRIVATE)

    fun writeLoginStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("save", status)
        editor.apply()
    }

    fun readLoginStatus(): Boolean {
        return sharedPreferences.getBoolean("save", false)
    }

    fun writePinStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("pinstatus", status)
        editor.apply()
    }

    fun readPinStatus(): Boolean {
        return sharedPreferences.getBoolean("pinstatus", false)
    }

    fun writeCheckinStatus(status: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("checkinstatus", status)
        editor.apply()
    }

    fun readCheckinStatus(): Boolean {
        return sharedPreferences.getBoolean("checkinstatus", false)
    }
}