package com.intec.template.data

data class PeticionCita(
    var clinicaId : Int,
    var specialityId : Int,
    var requestedTime : String,
    var patientName: String,
    var patientSurname : String,
    var patientPhone : String,
    var patientCountryCode : String,
    var patientEmail : String,
    var comments: String
)
