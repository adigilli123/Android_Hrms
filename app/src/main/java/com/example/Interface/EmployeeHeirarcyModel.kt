package com.example.Interface

data class EmployeeHeirarcyModel(
    val id: Int,
    val name: String,
    val position: String,
    val subordinates: List<EmployeeHeirarcyModel> = emptyList()
)