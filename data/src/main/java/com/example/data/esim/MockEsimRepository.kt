package com.example.data.esim

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class MockEsimRepository {

    /**
     * Simulate the downloadSubscription() method.
     * Returns a flow (Stream of data) rather than a single value.
     */

    //We use a flow for this as it is like a video stream, where it can return multiple values over time.
    //This is perfect for a progress bar where there status changes over time.

    //emit is used to push a new value into this flow stream.
    fun provisionProfile(activationCode: String) : Flow<EsimProvisioningState> = flow {

        //Handshake

        //emit() sends a piece of data down the pipeline to the UI
        emit(EsimProvisioningState.Provisioning("Connecting to SM-DP+ Server", 0.1f))
        delay(1500)

        //Validation
        if(activationCode.isBlank()){
            emit(EsimProvisioningState.Error("Invalid Activation Code"))
            return@flow //stops the flow
        }

        emit(EsimProvisioningState.Provisioning("Verifying Profile Metadata", 0.4f))
        delay(1500)

        //Download
        emit(EsimProvisioningState.Provisioning("Downloading Profile...", 0.7f))
        delay(2000)

        //Install
        emit(EsimProvisioningState.Provisioning("Installing Profile on eUICC...", 0.9f))
        delay(1500)

        //Success: Generate a random fake ICCID (SIM serial number)
        val mockIccid = "8901${Random.nextLong(10000000000000, 99999999999999)}"
    }
}