package com.anthony.yapewhatsapp.api.repository

import com.anthony.yapewhatsapp.api.ApiService
import com.anthony.yapewhatsapp.api.DeviceResponse

class DeviceRepository(private val apiService:ApiService) {
    suspend fun registerDevice(device:DeviceResponse) = apiService.registerDevice(device)
    suspend fun verifyDevice(mac:String) =apiService.verifyDevice(mac)
}