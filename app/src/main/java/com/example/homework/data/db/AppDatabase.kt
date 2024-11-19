package com.example.homework.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homework.data.db.dao.CryptoCurrencyDao
import com.example.homework.data.db.dao.FiatCurrencyDao
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import com.example.homework.data.db.entity.FiatCurrencyInfo

@Database(
    entities = [CryptoCurrencyInfo::class, FiatCurrencyInfo::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCryptoCurrencyDao(): CryptoCurrencyDao
    abstract fun getFiatCurrencyDao(): FiatCurrencyDao
}