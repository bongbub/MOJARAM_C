package com.example.mojaram

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val messages = mutableListOf<Message>()
    private var isTyping = false

    fun addMessage(message: Message) {
        if (isTyping) {
            removeTypingIndicator()
        }
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun showTypingIndicator() {
        if (!isTyping) {
            isTyping = true
            messages.add(Message("typing", "작성중..."))
            notifyItemInserted(messages.size - 1)
        }
    }

    fun removeTypingIndicator() {
        if (isTyping) {
            isTyping = false
            messages.removeAt(messages.size - 1)
            notifyItemRemoved(messages.size)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (messages[position].role) {
            "user" -> 0
            "bot" -> 1
            else -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> UserMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_user_message, parent, false)
            )
            1 -> BotMessageViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_bot_message, parent, false)
            )
            else -> TypingViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_typing_message, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UserMessageViewHolder -> holder.bind(messages[position])
            is BotMessageViewHolder -> holder.bind(messages[position])
            is TypingViewHolder -> holder.bind(messages[position])
        }
    }

    override fun getItemCount(): Int = messages.size

    class UserMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.userMessageTextView)
        fun bind(message: Message) {
            messageTextView.text = message.content
        }
    }

    class BotMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.botMessageTextView)
        fun bind(message: Message) {
            messageTextView.text = message.content
        }
    }

    class TypingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.typingMessageTextView)
        fun bind(message: Message) {
            messageTextView.text = message.content
        }
    }
}
