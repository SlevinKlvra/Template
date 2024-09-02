package com.intec.template.preferences

import android.content.SharedPreferences
import javax.inject.Inject

class PreferencesRepository @Inject constructor(private val sharedPreferences: SharedPreferences) {
    fun getBrokerIp(): String = sharedPreferences.getString("broker_ip", "tcp://10.14.0.182:1883") ?: "tcp://10.14.0.182:1883"
    fun setBrokerIp(ip: String) = sharedPreferences.edit().putString("broker_ip", ip).apply()

    // Agregar nuevos
    fun getBrokerPort(): String = sharedPreferences.getString("puerto", "1883") ?: "1883"
    fun setBrokerPort(puerto: String) = sharedPreferences.edit().putString("puerto", puerto).apply()

    fun getMqttUsuario(): String = sharedPreferences.getString("mqtt_usuario", "intecfull") ?: "intecfull"
    fun setMqttUsuario(usuarioMqtt: String) = sharedPreferences.edit().putString("mqtt_usuario", usuarioMqtt).apply()

    fun getMqttPassword(): String = sharedPreferences.getString("mqtt_password", "intecfullpassword") ?: "intecfullpassword"
    fun setMqttPassword(passwordMqtt: String) = sharedPreferences.edit().putString("mqtt_password", passwordMqtt).apply()

    fun getMqttQoS(): String = sharedPreferences.getString("mqtt_qos", "0") ?: "0"
    fun setMqttQoS(qosMqtt: String) = sharedPreferences.edit().putString("mqtt_qos", qosMqtt).apply()

    fun getMqttClient(): String = sharedPreferences.getString("mqtt_client", "Robot") ?: "Robot"
    fun setMqttClient(clientMqtt: String) = sharedPreferences.edit().putString("mqtt_client", clientMqtt).apply()

    fun getIdleWaitingTime(): Int = sharedPreferences.getInt("idle_waiting_time", 30)
    fun setIdleWaitingTime(idleWaitingTime: Int) = sharedPreferences.edit().putInt("idle_waiting_time", idleWaitingTime).apply()

    fun getMeetingTimeThreshold(): Int = sharedPreferences.getInt("waiting_meeting_time", 10)
    fun setMeetingTimeThreshold(waitingMeetingTimeThreshold: Int) = sharedPreferences.edit().putInt("waiting_meeting_time", waitingMeetingTimeThreshold).apply()

    fun getApiUsuario(): String = sharedPreferences.getString("api_usuario", "sergio.escudero@intecrobots.com") ?: "sergio.escudero@intecrobots.com"
    fun setApiUsuario(usuario: String) = sharedPreferences.edit().putString("api_usuario", usuario).apply()

    fun getApiPassword(): String = sharedPreferences.getString("api_password", "sec000611") ?: "sec000611"
    fun setApiPassword(passwordApi: String) = sharedPreferences.edit().putString("api_password", passwordApi).apply()

    fun getReturnDestination(): String = sharedPreferences.getString("return_destination", "espera") ?: "espera"
    fun setReturnDestination(returnDestination: String) = sharedPreferences.edit().putString("return_destination", returnDestination).apply()

    fun getCoordinateDeviation(): Float = sharedPreferences.getFloat("coordinate_deviation", 0.1f) ?: 0.1f
    fun setCoordinateDeviation(radiusDeviation: Float) = sharedPreferences.edit().putFloat("coordinate_deviation", radiusDeviation).apply()

    fun getNavigationTimeout(): Long = sharedPreferences.getLong("robot_timeout", 1000000) ?: 1000000
    fun setNavigationTimeout(timeoutNativagion: Long) = sharedPreferences.edit().putLong("robot_timeout", timeoutNativagion).apply()

    fun getStartHour(): String = sharedPreferences.getString("start_hour", "11:45") ?: "11:45"
    fun setStartHour(startHour: String) = sharedPreferences.edit().putString("start_hour", startHour).apply()

    fun getEndHour(): String = sharedPreferences.getString("end_hour", "11:27") ?: "11:27"
    fun setEndHour(endHour: String) = sharedPreferences.edit().putString("end_hour", endHour).apply()

    fun getToken(): String = sharedPreferences.getString("token", "") ?: ""
    fun setToken(token: String?) = sharedPreferences.edit().putString("token", token).apply()

    fun getLanguage(): String = sharedPreferences.getString("language", "") ?: ""
    fun setLanguage(language: String) = sharedPreferences.edit().putString("language", language).apply()

    fun getScheduleId(): Int = sharedPreferences.getInt("schedule_id", 0) ?: 0
    fun setScheduleId(scheduleId: Int) = sharedPreferences.edit().putInt("schedule_id", scheduleId).apply()

    fun getCompanyId(): Int = sharedPreferences.getInt("company_id", 0) ?: 0
    fun setCompanyId(companyId: Int) = sharedPreferences.edit().putInt("company_id", companyId).apply()

}