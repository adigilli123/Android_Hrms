package com.example.Interface

class LeaveavailableModel{
    var message: String? = null
    var data: ArrayList<Datum3>? = null
}

class Datum3 {
    var actualLeaves = 0
    var pendingLeaves = 0
    var busLeaveID: String? = null
    var _id: String? = null
    var leaveType: String? = null
    var description: Any? = null
}