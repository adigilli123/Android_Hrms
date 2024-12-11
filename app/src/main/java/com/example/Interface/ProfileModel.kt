package com.example.Interface


import java.util.Date

data class ProfileModel(
    var _id: String? = null,
    var employeeNumber: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var displayName: String? = null,
    var fullName: String? = null,
    var workEmail: String? = null,
    var dateOfBirth: Date? = null,
    var gender: String? = null,
    var maritalStatus: String? = null,
    var bloodGroup: String? = null,
    var physicallyHandicapped: String? = null,
    var nationality: String? = null,
    var mobilePhone: String? = null,
    var attendanceNumber: String? = null,
    var location: String? = null,
    var locationCountry: String? = null,
    var businessUnit: String? = null,
    var department: String? = null,
    var jobTitle: String? = null,
    var reportingTo: String? = null,
    var dottedLineManager: String? = null,
    var dateJoined: Date? = null,
    var leavePlan: String? = null,
    var timeType: String? = null,
    var workerType: String? = null,
    var shiftPolicyName: String? = null,
    var weekOffPolicyName: String? = null,
    var attendanceCaptureScheme: String? = null,
    var holidayListName: String? = null,
    var noticePeriod: String? = null,
    var employmentStatus: String? = null,
    var __v: Int = 0,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var password: String? = null
)