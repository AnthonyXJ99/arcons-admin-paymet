package com.anthony.yapewhatsapp.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("api/dispositivo")
    suspend fun registerDevice(@Body device:DeviceResponse): Response<ResponseStatus>

    @GET("api/dispositivo/{mac}")
    suspend fun verifyDevice(@Path("mac") mac:String):ResponseStatus


}