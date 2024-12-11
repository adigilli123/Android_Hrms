package com.example.Interface

import java.util.Date


class LeavetypeModel {
    var message: String? = null
    var data: ArrayList<Datum1>? = null
}

class Datum1 {
    var _id: String? = null
    var business_unit: String? = null
    var leave_type: String? = null
    var allocated_leaves = 0
    var max_limit = 0
    var description: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
}