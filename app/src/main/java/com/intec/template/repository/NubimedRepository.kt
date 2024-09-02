package com.intec.template.repository

import android.util.Log
import com.intec.template.api.NubimedApi
import com.intec.template.data.PeticionCita
import com.intec.template.util.Constants.TOKEN
import com.intec.template.util.Constants.USER_EMAIL
import retrofit2.Response
import javax.inject.Inject

class NubimedRepository @Inject constructor(
    private val nubimedApi: NubimedApi
){
    suspend fun setPeticionCita(peticionCita: PeticionCita): Response<Unit> {
        Log.d("API", peticionCita.toString())
        return nubimedApi.setPeticionCita(
            clinicaId = peticionCita.clinicaId,
            requestedTime = peticionCita.requestedTime,
            patientName = peticionCita.patientName,
            patientSurname = peticionCita.patientSurname,
            patientPhone = peticionCita.patientPhone,
            patientCountryCode = peticionCita.patientCountryCode,
            patientemail = peticionCita.patientEmail,
            comment = peticionCita.comments,
            specialityId = peticionCita.specialityId,
            token = TOKEN,
            userEmail = USER_EMAIL
        )
    }
}