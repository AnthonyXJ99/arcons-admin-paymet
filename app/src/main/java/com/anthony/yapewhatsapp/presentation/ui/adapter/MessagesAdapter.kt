package com.anthony.yapewhatsapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.MessageDetailsItemsBinding
import com.anthony.yapewhatsapp.domain.model.MessageModel

class MessagesAdapter(private val callback: MessageCallback): ListAdapter<MessageModel, MessagesAdapter.ViewHolder>(
    DiffUtilYapeMessageCallback
) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding=MessageDetailsItemsBinding.bind(view)
        fun render(message: MessageModel){
            binding.txtTitle.text=message.title
            binding.txtDate.text=message.date
            binding.txtMessage.text= message.message.toString()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context).inflate(R.layout.message_details_items,parent,false)
        return ViewHolder(layoutInflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message=getItem(position)
        holder.render(message)
        val binding=MessageDetailsItemsBinding.bind(holder.itemView)
        binding.iconButton.setOnClickListener {
            callback.onDeleteItem(message)
        }
    }
}

private object DiffUtilYapeMessageCallback: DiffUtil.ItemCallback<MessageModel>(){
    override fun areItemsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean =oldItem.id==newItem.id

    override fun areContentsTheSame(oldItem: MessageModel, newItem: MessageModel): Boolean {
        return oldItem==newItem
    }

}

interface MessageCallback{
    fun onDeleteItem(message:MessageModel)
}