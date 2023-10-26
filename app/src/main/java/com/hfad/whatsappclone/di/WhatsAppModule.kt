package com.hfad.whatsappclone.di

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hfad.whatsappclone.data.localDatabase.UserDao
import com.hfad.whatsappclone.data.localDatabase.UserDatabase
import com.hfad.whatsappclone.data.repository.AuthRepositoryImp
import com.hfad.whatsappclone.data.repository.UserRepositoryImp
import com.hfad.whatsappclone.domain.repository.AuthRepository
import com.hfad.whatsappclone.domain.repository.UserRepository
import com.hfad.whatsappclone.domain.use_case.AuthenticationUseCase
import com.hfad.whatsappclone.domain.use_case.ChatUseCase
import com.hfad.whatsappclone.domain.use_case.ContactsUseCase
import com.hfad.whatsappclone.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WhatsAppModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): UserDatabase {
        return Room.databaseBuilder(
            context, UserDatabase::class.java,
            Constants.userRoomDatabase
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: UserDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFirestore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImp(firebaseAuth, firebaseFirestore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCase(authRepository: AuthRepository): AuthenticationUseCase {
        return AuthenticationUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseFirestore: FirebaseFirestore,
        userDao: UserDao
    ): UserRepository {
        return UserRepositoryImp(firebaseFirestore, userDao)
    }

    @Provides
    @Singleton
    fun provideContactsUseCase(userRepository: UserRepository): ContactsUseCase {
        return ContactsUseCase(userRepository)
    }

    @Provides
    @Singleton
    fun provideChatUseCase(repository: UserRepository):ChatUseCase{
        return ChatUseCase(repository)
    }

}