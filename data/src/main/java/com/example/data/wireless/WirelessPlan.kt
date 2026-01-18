package com.example.data.wireless

data class WirelessPlan(
    val dataUsedGb: Double,
    val dataLimitGb: Double,
    val daysRemaining: Int,
    val planName: String
)