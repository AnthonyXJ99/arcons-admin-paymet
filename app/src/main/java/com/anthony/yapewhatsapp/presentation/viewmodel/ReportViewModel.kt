package com.anthony.yapewhatsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.anthony.yapewhatsapp.data.repository.MessageRepository
import com.anthony.yapewhatsapp.domain.model.MessageModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val repository:MessageRepository):ViewModel(){

    fun filterByApp(apps:List<String>,today:String): Flow<List<MessageModel>?> {
        return repository.filterByApp(apps,today)
    }
    fun filterByDate(apps:List<String>,start:String,end:String): Flow<List<MessageModel>?> {
        return repository.filterByDate(apps,start,end)
    }
}