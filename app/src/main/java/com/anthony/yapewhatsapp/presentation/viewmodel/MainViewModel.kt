package com.anthony.yapewhatsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.yapewhatsapp.api.repository.DeviceRepository
import com.anthony.yapewhatsapp.data.repository.UserRepository
import com.anthony.yapewhatsapp.domain.model.UserEntity
import com.anthony.yapewhatsapp.util.collectFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val userRepository: UserRepository,private val deviceRepository: DeviceRepository):ViewModel() {

    init {
        checkedUser()
    }

    fun isSession():Flow<UserEntity> = userRepository.findUser()

    private fun checkedUser(){
        viewModelScope.collectFlow(userRepository.findUser()) {user->
           if (user!=null){
               val device= deviceRepository.verifyDevice(user.mac)
               if (user.isActive){
                   if (device.status==200){
                       if (!device.data.isActive){
                           user.isActive=false
                           userRepository.update(user)
                       }
                   }
               }

           }
        }
    }



}