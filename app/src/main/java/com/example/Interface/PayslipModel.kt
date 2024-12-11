package com.example.Interface

data class PayslipModel(
    var payslip_url: String,
    var orgid: Int,
    var employee_id: Int,
    var deptid: Int,
    var designationid: Int,
    var paydate: String,
    var payslip_id: Int
)