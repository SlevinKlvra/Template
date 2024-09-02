package com.intec.template.data

data class WebSocketResponse(
    var event: String,
    var robotSN: String,
    var sala : String,
    var cliente: String,
    var message: String,
    var usuario: String
)
