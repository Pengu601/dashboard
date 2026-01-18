package com.example.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MockWirelessRepository
import com.example.data.WirelessPlan
import com.example.data.WirelessRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WirelessViewModel (
    private val repository: WirelessRepository = MockWirelessRepository()
) : ViewModel(){

    private val _uiState = MutableStateFlow<WirelessPlan?>(null)
    val uiState: StateFlow<WirelessPlan?> = _uiState.asStateFlow()

    init {
        fetchPlanData()
    }

    private fun fetchPlanData() {
        viewModelScope.launch {
            repository.getPlanDetails()
                .collect { plan ->
                    _uiState.value = plan
                }
        }
    }
}