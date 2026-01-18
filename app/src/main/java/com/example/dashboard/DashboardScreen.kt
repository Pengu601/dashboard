package com.example.dashboard // Ensure this matches your project

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.esim.EsimProvisioningState


//Modifier is a very strong tool that tells how a UI element how to look or behave
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: WirelessViewModel = viewModel()) {
    //Collect esimState
    val esimState by viewModel.esimState.collectAsStateWithLifecycle()

    //Scaffold (layout format for the screen, has predefined slots for easy template)
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Dashboard") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            )
        }
    ) { innerPadding ->
        //Content - use inner padding to ensure content isn't hidden behind the top bar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //Plan Card
            PlanOverviewCard()

            //Quick Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Button(
                onClick = {viewModel.activateEsim() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.AddCircle, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Activate Esim")
            }
        }

    }

    //Dialog
    EsimActivationDialog(
        state = esimState,
        onDismiss = {viewModel.resetEsimState()}
    )
}


//Card containing Data plan and current usage
@Composable
fun PlanOverviewCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Unlimited Data Plan", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Data Usage : 12.5 GB / 30 GB ", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = { .4f}, //hard coded for now
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsimActivationDialog(
    state: EsimProvisioningState,
    onDismiss: () -> Unit
) {
    if(state is EsimProvisioningState.Idle) return

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "eSIM Activation")},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when(state) {
                    is EsimProvisioningState.Provisioning -> {
                        CircularProgressIndicator(
                            progress = { state.progress },
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = state.stage, style = MaterialTheme.typography.bodyMedium)
                    }
                    is EsimProvisioningState.Success -> {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.Green,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("Profile Active")
                        Text("ICCID: ${state.iccid}", style = MaterialTheme.typography.bodyMedium)
                    }
                    else -> {}
                }
            }
        },
        confirmButton = {
            if(state is EsimProvisioningState.Success || state is EsimProvisioningState.Error){
                TextButton(onClick = onDismiss){
                    Text("Done")
                }
            }
        }

    )
}