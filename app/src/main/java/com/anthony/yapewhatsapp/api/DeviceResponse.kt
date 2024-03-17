package com.anthony.yapewhatsapp.api

import com.google.gson.annotations.SerializedName

data class DeviceResponse(
    @SerializedName("correo")
    val email: String,
    @SerializedName("mac")
    val mac: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("telefono")
    val phone: String,

    @SerializedName("estado")
    val isActive: Boolean
)