package com.intec.template.data

data class LoginResponse(
    val token: String,
    val robotId: Int,
    val companyId: Int,
    val roleId: String,
    val workScheduleId: Int
)