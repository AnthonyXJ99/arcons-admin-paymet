package com.anthony.yapewhatsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.yapewhatsapp.data.repository.MessageRepository
import com.anthony.yapewhatsapp.domain.model.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MessageRepository
):ViewModel(){
//    var message by mutableStateOf(MessageModel(id = 0, date = "", title = "", message = "", subMessage = ""))
//        private set

    val getAllMessages = repository.getAll()

    fun filterByApp(apps:List<String>,today:String): Flow<List<MessageModel>?> {
        return repository.filterByApp(apps,today)
    }
    fun filterByDate(apps:List<String>,start:String,end:String): Flow<List<MessageModel>?>{
        return repository.filterByDate(apps,start,end)
    }

    private var deleteMessage: MessageModel? =null

    fun insertMessage(message: MessageModel){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(message)
        }
    }

    fun deleteMessage(message: MessageModel){
        viewModelScope.launch(Dispatchers.IO) {
            deleteMessage=message
            repository.delete(message)
        }
    }
    fun deleteAllMessage(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }

    fun undoDeleteMessage(){
        deleteMessage?.let { message ->
            viewModelScope.launch(Dispatchers.IO){
                repository.insert(message)
            }
        }
    }
}