package com.example.Interface

import java.util.Date


data class ApplyleaveModel(
    var message: String? = null,
    var data: Data3? = null
)

data class Data3(
    var employeeNumber: String? = null,
    var orgId: String? = null,
    var businessUnitId: String? = null,
    var departmentId: String? = null,
    var leaveType: String? = null,
    var fromDate: Date? = null,
    var toDate: Date? = null,
    var fromDayType: String? = null,
    var toDayType: String? = null,
    var leavesCount: Int = 0,
    var reasonLeave: String? = null,
    var leaveStatus: String? = null,
    var notifyEmployees: ArrayList<NotifyEmployee2>? = null,
    var appliedDate: Date? = null,
    var _id: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var __v: Int = 0
)

data class NotifyEmployee2(
    var employeeNumber: Int = 0,
    var _id: String? = null
)
