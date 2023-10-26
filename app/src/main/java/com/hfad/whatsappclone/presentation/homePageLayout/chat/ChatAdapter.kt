package com.hfad.whatsappclone.presentation.homePageLayout.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.domain.model.ModelChat
import com.hfad.whatsappclone.util.OutlineProvider

class ChatAdapter(var listener: IChatView) :
    ListAdapter<ModelChat, ChatAdapter.ViewHolder>(ChatDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val modelChat = getItem(position)
        holder.bindView(modelChat, listener)
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(modelChat: ModelChat, listener: IChatView) {
            val chatCard: RelativeLayout = view.findViewById(R.id.parent)
            val chatName: TextView = view.findViewById(R.id.chatName)
            val chatImage: ImageView = view.findViewById(R.id.chatImage)
            val chatLastMessage: TextView = view.findViewById(R.id.chatLastMessage)
            val chatLastMessageTimeStamp: TextView =
                view.findViewById(R.id.chatLastMessageTimeStamp)
            chatImage.outlineProvider = OutlineProvider()
            chatImage.clipToOutline = true
            Glide.with(itemView.context).load(modelChat.chatImage).into(chatImage)
            chatName.text = modelChat.chatName
            chatLastMessage.text = modelChat.chatLastMessage
            chatLastMessageTimeStamp.text = modelChat.chatLastMessageTimestamp
            chatCard.setOnClickListener {
                listener.openMessageFragment(modelChat.chatId)
            }
        }
    }

    class ChatDiffUtil : DiffUtil.ItemCallback<ModelChat>() {
        override fun areItemsTheSame(oldItem: ModelChat, newItem: ModelChat): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ModelChat, newItem: ModelChat): Boolean {
            return oldItem.chatId == newItem.chatId
        }
    }

}