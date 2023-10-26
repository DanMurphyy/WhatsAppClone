package com.hfad.whatsappclone.presentation.homePageLayout.message

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.whatsappclone.databinding.MessageImageReceiverBinding
import com.hfad.whatsappclone.databinding.MessageImageSenderBinding
import com.hfad.whatsappclone.databinding.MessageTextReceiverBinding
import com.hfad.whatsappclone.databinding.MessageTextSenderBinding
import com.hfad.whatsappclone.domain.model.ModelMessage


class MessageAdapter(private var listener: IMessageView) :
    ListAdapter<ModelMessage, RecyclerView.ViewHolder>(MessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            VIEW_TYPE_TEXT_SENT -> TextMessageViewHolderSender(
                MessageTextSenderBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_TEXT_RECEIVED -> TextMessageViewHolderReceiver(
                MessageTextReceiverBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_IMAGE_SENT -> ImageMessageViewHolderSender(
                MessageImageSenderBinding.inflate(inflater, parent, false)
            )
            VIEW_TYPE_IMAGE_RECEIVED -> ImageMessageViewHolderReceiver(
                MessageImageReceiverBinding.inflate(inflater, parent, false)
            )
            else -> ImageMessageViewHolderSender(
                MessageImageSenderBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val messageItem = getItem(position)
        Log.d("MessageAdapter", "MessageType: ${messageItem.messageType}")
        when (holder) {
            is TextMessageViewHolderSender -> holder.bindView(messageItem)
            is TextMessageViewHolderReceiver -> holder.bindView(messageItem)
            is ImageMessageViewHolderSender -> holder.bindView(messageItem)
            is ImageMessageViewHolderReceiver -> holder.bindView(messageItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val messageItem = getItem(position)
        val userId = listener.getUserId()

        return when (messageItem.messageType) {
            "text" -> {
                if (messageItem.messageSender == userId) {
                    VIEW_TYPE_TEXT_SENT
                } else {
                    VIEW_TYPE_TEXT_RECEIVED
                }
            }
            "image" -> {
                if (messageItem.messageSender == userId) {
                    VIEW_TYPE_IMAGE_SENT
                } else {
                    VIEW_TYPE_IMAGE_RECEIVED
                }
            }
            else -> VIEW_TYPE_TEXT_RECEIVED
        }
    }


    // Constants for view types
    private val VIEW_TYPE_TEXT_SENT = 0
    private val VIEW_TYPE_TEXT_RECEIVED = 1
    private val VIEW_TYPE_IMAGE_SENT = 2
    private val VIEW_TYPE_IMAGE_RECEIVED = 3

    class ImageMessageViewHolderSender(var binding: MessageImageSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(messageModel: ModelMessage) {
            Glide.with(itemView.context).load(messageModel.messageData).into(binding.imageSender)
        }
    }

    class ImageMessageViewHolderReceiver(var binding: MessageImageReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(messageModel: ModelMessage) {
            Glide.with(itemView.context).load(messageModel.messageData).into(binding.imageReceiver)
        }
    }

    class TextMessageViewHolderSender(var binding: MessageTextSenderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(messageModel: ModelMessage) {
            binding.textSender.text = messageModel.messageData
        }
    }

    class TextMessageViewHolderReceiver(var binding: MessageTextReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindView(messageModel: ModelMessage) {
            binding.textReceiver.text = messageModel.messageData
        }
    }

    class MessageDiffUtil : DiffUtil.ItemCallback<ModelMessage>() {
        override fun areItemsTheSame(oldItem: ModelMessage, newItem: ModelMessage): Boolean {
            // Compare based on attributes that make an item the same
            return oldItem.messageType == newItem.messageType &&
                    oldItem.messageData == newItem.messageData &&
                    oldItem.messageSender == newItem.messageSender &&
                    oldItem.messageReceiver == newItem.messageReceiver
        }

        override fun areContentsTheSame(oldItem: ModelMessage, newItem: ModelMessage): Boolean {
            // Compare based on contents (e.g., text or image data)
            return oldItem == newItem
        }
    }
}