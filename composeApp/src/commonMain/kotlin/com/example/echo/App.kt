package com.example.echo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.echo.viewmodels.NearbyDevicesViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.uuid.ExperimentalUuidApi

@Composable
@Preview
fun App() {
    MaterialTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            CollektiveNearbyDevices(modifier = Modifier.padding(innerPadding))
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun CollektiveNearbyDevices(modifier: Modifier) {
    val viewModel = remember { NearbyDevicesViewModel() }

    val devices by viewModel.dataFlow.collectAsState()
    val connection by viewModel.connectionFlow.collectAsState()
    val uuid = viewModel.deviceId

    LaunchedEffect(Unit) {
        viewModel.startCollektiveProgram()
    }

    Column(
        modifier,
    ) {
        Text("ID: $uuid")
        Text("Status: $connection")
        Spacer(Modifier.height(8.dp))
        Text("Devices:")
        devices.forEach { deviceId ->
            Text("- $deviceId")
        }
    }
}
