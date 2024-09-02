package com.intec.template.api

import com.intec.template.data.CreateRoomRequest
import com.intec.template.data.LoginRequest
import com.intec.template.data.LoginResponse
import com.intec.template.data.ScheduleRequest
import com.intec.template.data.ScheduleResponse
import com.intec.template.data.ValidateCode
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface IntecApi {
    @POST("visits/validateCode")
    suspend fun validateCode(
        @Body validateCode: ValidateCode,
        @Header("Authorization") token: String,
    ): Response<Unit>

    @POST("robots/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
    @GET("work-schedules")
    suspend fun schedules(
        @Header("Authorization") token: String
    ): Response<List<ScheduleResponse>>
    @PATCH("work-schedules/{id}")
    suspend fun updateSchedules(
        @Path("id") id:Int,
        @Body scheduleRequest: ScheduleRequest,
        @Header("Authorization") token: String
    ): Response<Unit>
    @POST("rooms/createRooms")
    suspend fun createRooms(
        @Body createRoom: List<CreateRoomRequest>,
        @Header("Authorization") token: String
    ): Response<Unit>


}