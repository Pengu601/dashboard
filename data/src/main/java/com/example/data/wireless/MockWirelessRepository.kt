package com.example.data.wireless

import com.example.data.wireless.WirelessPlan
import com.example.data.wireless.WirelessRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockWirelessRepository : WirelessRepository {
    override fun getPlanDetails(): Flow<WirelessPlan> = flow {
        delay(1000)
        emit(
            WirelessPlan(
                dataUsedGb = 20.0,
                dataLimitGb = 50.0,
                daysRemaining = 14,
                planName = "Unlimited"
            )
        )
    }
}