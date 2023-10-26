package com.hfad.whatsappclone.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class ModelUser(
    @PrimaryKey
    val userId: String = "",
    var userName: String? = "",
    var userImage: String? = "",
    var userNumber: String? = "",
    var userStatus: String? = ""
)
