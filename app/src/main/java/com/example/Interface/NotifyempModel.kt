package com.example.Interface

import java.util.Date


class NotifyempModel {
    var message: String? = null
    var data: ArrayList<Datum>? = null
}

class Datum {
    var _id: String? = null
    var orgId: String? = null
    var businessUnitId: String? = null
    var employeeNumber: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var status: String? = null
    var employmentStatus: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
    var photo: String? = null

}

