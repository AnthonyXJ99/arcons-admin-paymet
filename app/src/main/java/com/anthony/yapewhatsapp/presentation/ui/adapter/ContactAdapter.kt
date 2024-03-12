package com.anthony.yapewhatsapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.ContactItemsBinding
import com.anthony.yapewhatsapp.domain.model.Contact

class ContactAdapter(private val callback: ContactCallback):ListAdapter<Contact, ContactAdapter.ViewHolder>(
    DiffUtilContact
) {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding= ContactItemsBinding.bind(view)
        fun render(contact: Contact){
            binding.contactAvatar.avatarInitials= contact.name[0].toString()
            binding.txtContactName.text=contact.name
            binding.txtContacNumber.text=contact.number
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.contact_items,parent,false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact=getItem(position)
        holder.render(contact)
        val binding= ContactItemsBinding.bind(holder.itemView)
        binding.btnDeleteContact.setOnClickListener {
            callback.onDeleteItem(contact)
        }
    }
}

private object DiffUtilContact: DiffUtil.ItemCallback<Contact>(){
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean =oldItem.id==newItem.id

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem==newItem
    }

}

interface ContactCallback{
    fun onDeleteItem(contact: Contact)
}