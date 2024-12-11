package com.example.Interface

import java.util.Date

class ClockOutModel {
    var message: String? = null
    var attendance: Attendance? = null

    class Attendance {
        var _id: String? = null
        var employeeNumber: String? = null
        var date: Date? = null
        var clockInTime: Date? = null
        var clockOutTime: Date? = null
        var latitude = 0.0
        var longitude = 0.0
        var status: String? = null
        var lateLogin = false
        var totalHoursWorked = 0.0
        var comments: String? = null
        var createdAt: Date? = null
        var updatedAt: Date? = null
        var __v = 0
    }
}


