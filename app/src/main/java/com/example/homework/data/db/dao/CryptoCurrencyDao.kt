package com.example.homework.data.db.dao

import androidx.room.*
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface CryptoCurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun multipleInserts(list: List<CryptoCurrencyInfo>)

    @Query("DELETE FROM crypto_currency_info")
    fun deleteAll()

    @Query("SELECT * FROM crypto_currency_info ORDER BY ID ASC")
    fun getAll(): Flow<List<CryptoCurrencyInfo>>
}