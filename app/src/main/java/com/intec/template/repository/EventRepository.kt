package com.intec.template.repository

import android.media.Image
import com.intec.template.data.DataLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventRepository {
    private val _chargingRequest = MutableSharedFlow<Boolean>(0)
    val chargingRequest: SharedFlow<Boolean> = _chargingRequest.asSharedFlow()

    private val _scheduleRequest = MutableSharedFlow<Boolean>(0)
    val scheduleRequest: SharedFlow<Boolean> = _scheduleRequest.asSharedFlow()

    private val _wakeUpRequest = MutableSharedFlow<Boolean>(0)
    val wakeUpRequest: SharedFlow<Boolean> = _wakeUpRequest.asSharedFlow()

    private val _isFollowingRequest = MutableSharedFlow<Boolean>()
    val isFollowingRequest: SharedFlow<Boolean> = _isFollowingRequest.asSharedFlow()

    private val _isBatteryRequest = MutableSharedFlow<Int>()
    val isBatteryRequest: SharedFlow<Int> = _isBatteryRequest.asSharedFlow()

    private val _isAvoidRequest = MutableSharedFlow<String>()
    val isAvoidRequest: SharedFlow<String> = _isAvoidRequest.asSharedFlow()
    private val _mqttListener = MutableSharedFlow<String>()
    val mqttListener: SharedFlow<String> = _mqttListener.asSharedFlow()

    private val _isImageRequest = MutableSharedFlow<Image>()
    val isImageRequest: SharedFlow<Image> = _isImageRequest.asSharedFlow()

    private val _logsRequest = MutableSharedFlow<Boolean>()
    val logsRequest: SharedFlow<Boolean> = _logsRequest.asSharedFlow()

    private val _crearLogsRequest = MutableSharedFlow<DataLog>()
    val crearLogsRequest: SharedFlow<DataLog> = _crearLogsRequest.asSharedFlow()

    suspend fun requestCharging() {
        _chargingRequest.emit(true)
    }
    suspend fun requestSchedule(schedule : Boolean) {
        _scheduleRequest.emit(schedule)
    }
    suspend fun requestWakeUp() {
        _wakeUpRequest.emit(true)
    }

    suspend fun requestIsFollowing(following: Boolean) {
        _isFollowingRequest.emit(following)
    }
    suspend fun requestBatteryLevel(battery : Int) {
        _isBatteryRequest.emit(battery)
    }
    suspend fun requestIsAvoid(avoid: String) {
        _isAvoidRequest.emit(avoid)
    }
    suspend fun requestImage(image : Image) {
        _isImageRequest.emit(image)
    }
    suspend fun requestLogs(send: Boolean) {
        _logsRequest.emit(send)
    }
    suspend fun requestCrearLogs(log: DataLog) {
        _crearLogsRequest.emit(log)
    }
    suspend fun MqttListener(connection : String){
        _mqttListener.emit(connection)
    }


}