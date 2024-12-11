package com.example.Interface

import java.util.Date


class LeaveHistoryModel {
    var message: String? = null
    var data: Data2? = null
}

class Data2 {
    var _id: String? = null
    var employeeNumber: String? = null
    var orgId: String? = null
    var businessUnitId: String? = null
    var departmentId: String? = null
    var leaveType: String? = null
    var fromDate: Date? = null
    var toDate: Date? = null
    var fromDayType: String? = null
    var toDayType: String? = null
    var leavesCount = 0
    var reasonLeave: String? = null
    var leaveStatus: String? = null
    var notifyEmployees: ArrayList<NotifyEmployee>? = null
    var appliedDate: Date? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
}


class NotifyEmployee {
    var employeeNumber = 0
    var _id: String? = null
}
