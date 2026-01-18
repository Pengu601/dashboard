package com.example.data.esim

//Use sealed as it forces compiler to throw message if we forget to handle one of these classes or objects
//This is because sealed tells compiler that these are the ONLY possible types of EsimProvisioningState that will exist

//A data class is when you need to carry data inside the state
//A data object is when the state has no extra data (basically a singleton)
sealed interface EsimProvisioningState {
    data object Idle : EsimProvisioningState
    data class Provisioning(val stage: String, val progress: Float) : EsimProvisioningState
    data class Success(val iccid: String) : EsimProvisioningState
    data class Error(val message: String) : EsimProvisioningState
}