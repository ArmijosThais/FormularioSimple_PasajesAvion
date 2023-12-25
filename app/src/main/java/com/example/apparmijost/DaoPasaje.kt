package com.example.apparmijost

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DaoPasaje {
    @GET("Pasajes.json")
    fun getPasajes():Call<List<Pasaje>>

    @POST("Pasajes.json")
    fun saveNewPasaje(@Body pasaje: Pasaje):Call<Pasaje>

    @DELETE("Pasajes/{id}.json")
    fun deletePasaje(@Path("id") id:String):Call<Pasaje>

    @PUT("Pasajes/{id}.json")
    fun updatePasaje(@Path("id") id:String, @Body pasaje: Pasaje):Call<Pasaje>
}