package com.anthony.yapewhatsapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthony.yapewhatsapp.databinding.FragmentContactsBinding
import com.anthony.yapewhatsapp.domain.model.Contact
import com.anthony.yapewhatsapp.presentation.ui.adapter.ContactAdapter
import com.anthony.yapewhatsapp.presentation.ui.adapter.ContactCallback
import com.anthony.yapewhatsapp.presentation.viewmodel.ContactViewModel
import com.anthony.yapewhatsapp.util.TextFieldValidation
import com.anthony.yapewhatsapp.util.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsView : Fragment(),ContactCallback {

    private lateinit var binding: FragmentContactsBinding
    private val contactViewModel by viewModels<ContactViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentContactsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val manager= LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
        val adapter= ContactAdapter(this)
        binding.rclContacts.layoutManager=manager
        binding.rclContacts.adapter=adapter


        binding.txtNumberContact.addTextChangedListener(TextFieldValidation(binding.txtNumberContact,binding.txtNumberContact,binding.outlinedTextFieldNumberContact))
        /**get contacts**/

        lifecycleScope.collectFlow(contactViewModel.getAllContacts){contacts ->
            if(contacts!=null){
                adapter.submitList(contacts)
            }
        }


        /**add contact**/
        binding.btnAddContact.setOnClickListener {
            if (binding.txtNumberContact.text.isNullOrBlank()){
                Toast.makeText(requireContext().applicationContext,"El numero es obligatorio",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val number= binding.txtNumberContact.text
            var name= ""

            name = if(binding.txtContactNameAdd.text.isNullOrBlank()){
                number.toString()
            }else{
                binding.txtContactNameAdd.text.toString()
            }

            lifecycleScope.launch(Dispatchers.IO){
                val contact= Contact(name=name.toString(), number = number.toString())
                contactViewModel.insertContact(contact)
            }
            clearForm()
        }
    }
    private fun clearForm(){
        if (binding.txtNumberContact.text!!.isNotBlank()){
            binding.txtNumberContact.text=null
        }
        if (binding.txtContactNameAdd.text!!.isNotBlank()){
            binding.txtContactNameAdd.text=null
        }


    }

    override fun onDeleteItem(contact: Contact) {
        lifecycleScope.launch(Dispatchers.IO) {
            contactViewModel.deleteContact(contact)
        }
    }

}