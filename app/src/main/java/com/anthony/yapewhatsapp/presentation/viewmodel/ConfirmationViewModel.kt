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
class ConfirmationViewModel @Inject constructor(private val userRepository: UserRepository, private val deviceRepository: DeviceRepository):ViewModel() {

    private fun isSession(): Flow<UserEntity> = userRepository.findUser()

    suspend fun verificationDevice(mac:String) = deviceRepository.verifyDevice(mac)

    fun updateUser(){
        viewModelScope.collectFlow(isSession()){user->
            if (user!=null){
                user.isActive=true
                userRepository.update(user)
            }
        }
    }


}