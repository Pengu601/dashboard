package com.example.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.esim.EsimProvisioningState
import com.example.data.esim.MockEsimRepository
import com.example.data.wireless.MockWirelessRepository
import com.example.data.wireless.WirelessPlan
import com.example.data.wireless.WirelessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WirelessViewModel (

    //inject repository
    private val esimRepository : MockEsimRepository = MockEsimRepository()
) : ViewModel() {

    //_esimState is mutable and private so only this viewModel can see and change it
    private val _esimState = MutableStateFlow<EsimProvisioningState>(EsimProvisioningState.Idle)

    //esimState is immutable and public. This is what the UI watches
    val esimState : StateFlow<EsimProvisioningState> = _esimState.asStateFlow()

    fun activateEsim(activationCode : String = "LPA:1\$sm-dp.carrier.com$12345"){

        //viewModelScope ensures this task dies if user closes the app, preventing memory leaks
        viewModelScope.launch {
            esimRepository.provisionProfile(activationCode)
                .collect { newState ->
                    //Every time the repo emits a new state, we update the value
                    _esimState.value = newState
                }
        }
    }

    //Sets Esim state back to idle
    fun resetEsimState(){
        _esimState.value = EsimProvisioningState.Idle
    }
}