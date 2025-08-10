package com.example.echo.viewmodels

import com.diamondedge.logging.logging
import com.example.echo.network.MqttMailbox
import it.unibo.collektive.Collektive
import it.unibo.collektive.aggregate.api.neighboring
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.seconds
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class NearbyDevicesViewModel(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val log = logging("VIEWMODEL")

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    @OptIn(ExperimentalUuidApi::class)
    private val _dataFlow = MutableStateFlow<Set<Uuid>>(emptySet())
    @OptIn(ExperimentalUuidApi::class)
    val dataFlow: StateFlow<Set<Uuid>> = _dataFlow.asStateFlow()

    private val _connectionFlow = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionFlow: StateFlow<ConnectionState> = _connectionFlow.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    val deviceId = Uuid.random()

    enum class ConnectionState { CONNECTED, DISCONNECTED }

    @OptIn(ExperimentalUuidApi::class)
    private suspend fun collektiveProgram(): Collektive<Uuid, Set<Uuid>> =
        Collektive(deviceId,
            MqttMailbox(deviceId, host = "broker.hivemq.com", dispatcher = dispatcher)
        ) {
            neighboring(localId).neighbors.toSet()
        }

    @OptIn(ExperimentalUuidApi::class)
    fun startCollektiveProgram() {
        scope.launch {
            log.i{"Starting Collektive program..."}
            _connectionFlow.value = ConnectionState.CONNECTED
            val program = collektiveProgram()
            log.i{"Collektive program started..."}
            while (isActive) {
                val newResult = program.cycle()
                _dataFlow.value = newResult
                delay(1.seconds)
                log.i { "New nearby devices: $newResult" }
            }
        }
    }
}
