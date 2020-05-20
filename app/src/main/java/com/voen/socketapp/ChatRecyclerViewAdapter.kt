package com.voen.socketapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.chat_item.view.*

class ChatRecyclerViewAdapter(private val chats: List<ChatMessage>) :
    RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view)
    }

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    class ChatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(chatMessage: ChatMessage) {
            view.tv_chat_message.text = chatMessage.message
        }
    }
}