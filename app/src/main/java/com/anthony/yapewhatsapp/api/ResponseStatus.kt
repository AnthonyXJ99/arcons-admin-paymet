package com.anthony.yapewhatsapp.api

import com.google.gson.annotations.SerializedName

data class ResponseStatus(

    @SerializedName("data")
    val data: DeviceResponse,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: Int
)