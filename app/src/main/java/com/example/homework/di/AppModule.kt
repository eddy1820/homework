package com.example.homework.di

import android.content.Context
import androidx.room.Room
import com.example.homework.data.db.AppDatabase
import com.example.homework.base.BaseApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val DATABASE_NAME = "app_db"

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideCryptoCurrencyDao(db: AppDatabase) = db.getCryptoCurrencyDao()

    @Singleton
    @Provides
    fun provideFiatCurrencyDao(db: AppDatabase) = db.getFiatCurrencyDao()

}