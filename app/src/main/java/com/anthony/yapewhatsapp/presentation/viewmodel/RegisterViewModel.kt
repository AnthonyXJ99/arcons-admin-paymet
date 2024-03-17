package com.anthony.yapewhatsapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.yapewhatsapp.api.DeviceResponse
import com.anthony.yapewhatsapp.api.repository.DeviceRepository
import com.anthony.yapewhatsapp.data.repository.UserRepository
import com.anthony.yapewhatsapp.domain.model.UserEntity
import com.anthony.yapewhatsapp.util.toUserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val deviceRepository: DeviceRepository, private val userRepository: UserRepository):ViewModel(){

    private val messageResponse = MutableLiveData<String>()
    fun getMessageResponse() = messageResponse

    private val statusResponse = MutableLiveData<Int>()

    fun getStatusResponse()= statusResponse

    suspend fun registerDevice(device:DeviceResponse){
        viewModelScope.launch(Dispatchers.IO){
            val response= deviceRepository.registerDevice(device)
            when(response.isSuccessful){
                true ->{
                    with(response.body()!=null) {
                        if (response.body()?.status==200){
                            val user= device.toUserEntity()
                            insertUser(user)
                            Log.d("REGISTRO DEL DISPOSITIVO","${response.body()}")
                            messageResponse.postValue(response.body()?.message.toString())
                            statusResponse.postValue( response.body()?.status)
                        }else{
                            messageResponse.postValue(response.body()?.message.toString())
                            statusResponse.postValue( response.body()?.status)
                        }

                    }
                }
                else ->{
                    messageResponse.postValue(response.message())
                    statusResponse.postValue(response.code())

                }
            }
        }
    }

    private fun insertUser(user:UserEntity){
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.insert(user)
        }
    }

}