package com.anthony.yapewhatsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthony.yapewhatsapp.data.repository.ContactRepository
import com.anthony.yapewhatsapp.domain.model.Contact
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor( private val repository: ContactRepository): ViewModel() {

    val getAllContacts = repository.getAll()


    private var deleteMessage: Contact? =null

    fun insertContact(contact: Contact){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(contact)
        }
    }

    fun deleteContact(contact: Contact){
        viewModelScope.launch(Dispatchers.IO) {
            deleteMessage=contact
            repository.delete(contact)
        }
    }
    fun deleteAllContacts(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAll()
        }
    }
}