package com.example.data

import com.example.data.wireless.MockWirelessRepository
import org.junit.Test
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.flow.first
class WirelessRepositoryTest {
    private val repository = MockWirelessRepository()

    @Test
    fun `getPlanDetails should return correct plain information`() = runTest {

        val result = repository.getPlanDetails().first()

        assertEquals("Unlimited", result.planName)
        assertEquals(50.0, result.dataLimitGb)
        assertTrue(result.dataUsedGb < result.dataLimitGb)
    }
}