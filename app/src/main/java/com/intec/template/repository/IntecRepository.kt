package com.intec.template.repository

import android.util.Log
import com.intec.template.api.IntecApi
import com.intec.template.api.NubimedApi
import com.intec.template.data.CreateRoomRequest
import com.intec.template.data.LoginRequest
import com.intec.template.data.LoginResponse
import com.intec.template.data.PeticionCita
import com.intec.template.data.ScheduleRequest
import com.intec.template.data.ScheduleResponse
import com.intec.template.data.ValidateCode
import com.intec.template.util.Constants.TOKEN
import com.intec.template.util.Constants.USER_EMAIL
import retrofit2.Response
import javax.inject.Inject

class IntecRepository  @Inject constructor(
    private val intecApi: IntecApi
){
    suspend fun login(loginRequest: LoginRequest): Response<LoginResponse> {
        Log.d("form", loginRequest.toString())
        return intecApi.login(
            loginRequest
        )
    }
    suspend fun validateCode(validateCode: ValidateCode, token: String): Response<Unit> {
        //Log.d("API", peticionCita.toString())
        return intecApi.validateCode(
            validateCode = validateCode,
            token = token
        )
    }
    suspend fun getSchedule(token: String): Response<List<ScheduleResponse>>{
        return intecApi.schedules(token)
    }
    suspend fun updateSchedule(id: Int, scheduleRequest: ScheduleRequest, token: String): Response<Unit>{
        return intecApi.updateSchedules(id, scheduleRequest, token)
    }
    suspend fun createRooms (createRoomRequest: List<CreateRoomRequest>, token:String):Response<Unit>{
        return intecApi.createRooms(createRoomRequest, token)
    }
}