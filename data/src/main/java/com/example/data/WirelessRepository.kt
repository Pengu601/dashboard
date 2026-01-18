package com.example.data

import kotlinx.coroutines.flow.Flow

interface WirelessRepository {
    fun getPlanDetails(): Flow<WirelessPlan>
}