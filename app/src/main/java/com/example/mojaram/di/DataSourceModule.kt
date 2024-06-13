package com.example.mojaram.di

import android.content.Context
import android.content.SharedPreferences
import com.example.mojaram.data.FirebaseDataSource
import com.example.mojaram.data.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun providesFireStore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun provideSharePrefrences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("MOJARAM_PREFS", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providePreferenceManager(sharedPreferences: SharedPreferences): PreferenceManager =
        PreferenceManager(sharedPreferences)

    @Singleton
    @Provides
    fun provideFirebaseDataSource(
        firestore: FirebaseFirestore
    ): FirebaseDataSource = FirebaseDataSource(firestore)
}