package com.hfad.whatsappclone.data.localDatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hfad.whatsappclone.domain.model.ModelUser

@Database(entities = [ModelUser::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}