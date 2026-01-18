package com.example.data.wireless

import kotlinx.coroutines.flow.Flow

interface WirelessRepository {
    fun getPlanDetails(): Flow<WirelessPlan>
}