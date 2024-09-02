package com.intec.template.api

import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface NubimedApi {
    @POST("cita_peticiones")
    suspend fun setPeticionCita(
        @Query("clinica_id") clinicaId: Int,
        @Query("requested_time") requestedTime: String,
        @Query("patient_name") patientName: String,
        @Query("patient_surname") patientSurname: String,
        @Query("patient_phone") patientPhone: String,
        @Query("patient_email") patientemail: String,
        @Query("comments") comment: String,
        @Query("patient_country_calling_code") patientCountryCode: String,
        @Query("speciality_id") specialityId: Int,
        @Header("X-User-Token") token: String,
        @Header("X-User-Email") userEmail: String
    ): Response<Unit>
}