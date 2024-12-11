package com.example.Interface


data class ApplyleaveBodyModel(
    var fromDayType: String? = null,
    var toDayType: String? = null,
    var fromDate: String? = null,
    var toDate: String? = null,
    var leaveType: String? = null,
    val leavesCount: Double,  // Change to Int
    var notifyEmployees: ArrayList<NotifyEmployee1>? = null,
    var reasonLeave: String? = null
)

data class NotifyEmployee1(
    var employeeNumber: Int = 0
)
