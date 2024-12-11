package com.example.Interface

import java.util.Date


class SingleEmployeeModel {
    var message: String? = null
    var data: Data? = null
}


class Contactdata {
    var _id: String? = null
    var orgId: String? = null
    var businessUnitId: String? = null
    var departmentId: String? = null
    var employeeNumber: String? = null
    var mobilePhone: String? = null
    var workEmail: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
}


class Data {
    var employeedata: Employeedata? = null
    var contactdata: Contactdata? = null
    var jobdata: Jobdata? = null
    var expdata: Expdata? = null
}


class Education {
    var degree: String? = null
    var institution: String? = null
    var yearOfPassing: String? = null
    var specialization: String? = null
    var grade: String? = null
    var _id: String? = null
    var document: String? = null
}


class Employeedata {
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
    var reportingTo: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
    var photo: String? = null
}

class Expdata {
    var professionalSummary: ProfessionalSummary? = null
    var _id: String? = null
    var employeeNumber: String? = null
    var experiences: ArrayList<Experience>? = null
    var education: ArrayList<Education>? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0
}


class Experience {
    var companyName: String? = null
    var role: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var document: String? = null
    var jobResponsibility: String? = null
    var skills: String? = null
    var _id: String? = null
    var achievments: String? = null
}


class Jobdata {
    var _id: String? = null
    var orgId: String? = null
    var businessUnitId: String? = null
    var departmentId: String? = null
    var employeeNumber: String? = null
    var businessUnitName: String? = null
    var departmentName: String? = null
    var jobTitle: String? = null
    var jobTitleId: Any? = null
    var shiftPolicy: String? = null
    var holidayList: String? = null
    var leavePlan: String? = null
    var noticePeriod: String? = null
    var createdAt: Date? = null
    var updatedAt: Date? = null
    var __v = 0

}


class ProfessionalSummary {
    var qualifications: String? = null
    var keyskills: String? = null
    var carrerObjectives: String? = null
    var highlights: String? = null
}