package com.example.apparmijost

import com.google.firebase.database.Exclude
import java.sql.Date
import java.sql.Time

data class Pasaje(
    val pasajero:String,
    val salida:String,
    val destino:String,
    val dia: String,
    val hora: String,
    @Exclude
    val id:String
):java.io.Serializable

