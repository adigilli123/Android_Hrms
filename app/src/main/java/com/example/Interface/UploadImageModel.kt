package com.example.Interface

import java.util.Date

class UploadImageModel {

    var message: String? = null
    var uploadimage: Uploadimage? = null
}

class Uploadimage {
    var _id: String? = null
    var orgId: String? = null
    var businessUnitId: String? = null
    var departmentId: String? = null
    var employeeNumber: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var dateOfBirth: Date? = null
    var gender: String? = null
    var maritalStatus: String? = null
    var status: String? = null
    var employmentStatus: String? = null
    var dateJoined: Date? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
    var photo: String? = null
}