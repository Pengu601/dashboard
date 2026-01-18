package com.example.data

data class WirelessPlan(
    val dataUsedGb: Double,
    val dataLimitGb: Double,
    val daysRemaining: Int,
    val planName: String
)