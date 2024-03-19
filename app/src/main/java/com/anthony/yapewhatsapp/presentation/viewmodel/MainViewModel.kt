package com.anthony.yapewhatsapp.presentation.viewmodel

import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.yapewhatsapp.api.repository.DeviceRepository
import com.anthony.yapewhatsapp.data.repository.UserRepository
import com.anthony.yapewhatsapp.domain.model.UserEntity
import com.anthony.yapewhatsapp.util.collectFlow
import com.anthony.yapewhatsapp.util.toUserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository,private val deviceRepository: DeviceRepository, private val context:Context):ViewModel() {

//    init {
//        checkedUser()
//    }

    fun isSession():Flow<UserEntity> = userRepository.findUser()

    fun checkedUser(){
        Log.d("XDXDXD","DASSSSSSSSSSSS")
        viewModelScope.collectFlow(userRepository.findUser()) {user->
            val mac=getMac()
            val device= deviceRepository.verifyDevice(mac)
           if (user!=null){
               if (user.isActive){
                   if (device.status==200){
                       if (!device.data.isActive){
                           user.isActive=false
                           userRepository.update(user)
                       }
                   }
               }
           }else{
               if (device.status==200 || device.status==400){
                   if(device.message=="Dispositivo no Encontrado"){
                       return@collectFlow
                   }
                   val entity=device.data.toUserEntity()
                   userRepository.insert(entity)
               }
           }
        }
    }

    private fun getMac(): String {
        return Settings.Secure.getString(context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
    }



}