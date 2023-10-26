package com.hfad.whatsappclone.data.localDatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hfad.whatsappclone.domain.model.ModelUser
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<ModelUser>>

    @Query("SELECT * FROM users WHERE userId = :userId")
    fun getUserById(userId: String): Flow<ModelUser>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(modelUser: ModelUser)
}