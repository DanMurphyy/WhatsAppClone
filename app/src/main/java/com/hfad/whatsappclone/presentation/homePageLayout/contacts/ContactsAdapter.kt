package com.hfad.whatsappclone.presentation.homePageLayout.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hfad.whatsappclone.R
import com.hfad.whatsappclone.domain.model.ModelUser
import com.hfad.whatsappclone.util.OutlineProvider

class ContactsAdapter : ListAdapter<ModelUser, ContactsAdapter.ViewHolder>(ContactDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bindView(contact)
    }

    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        fun bindView(modelUser: ModelUser) {
            val userName: TextView = view.findViewById(R.id.txtUserName)
            val userStatus: TextView = view.findViewById(R.id.txtUserStatus)
            val userProfile: ImageView = view.findViewById(R.id.userImage)
            userProfile.outlineProvider = OutlineProvider()
            userProfile.clipToOutline = true
            userName.text = modelUser.userName
            userStatus.text = modelUser.userStatus
            Glide.with(itemView.context).load(modelUser.userImage).into(userProfile)
        }
    }

    class ContactDiffUtil : DiffUtil.ItemCallback<ModelUser>() {
        override fun areItemsTheSame(oldItem: ModelUser, newItem: ModelUser): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ModelUser, newItem: ModelUser): Boolean {
            return oldItem == newItem
        }
    }
}