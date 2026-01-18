package com.example.dashboard // Ensure this matches your project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: WirelessViewModel = viewModel()) {
    val plan by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Lumina Wireless") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            if (plan != null) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = plan!!.planName, style = MaterialTheme.typography.headlineSmall)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Usage: ${plan!!.dataUsedGb} / ${plan!!.dataLimitGb} GB")

                        // Visual indicator for EchoStar "Innovation"
                        LinearProgressIndicator(
                            progress = { (plan!!.dataUsedGb / plan!!.dataLimitGb).toFloat() },
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        )
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}