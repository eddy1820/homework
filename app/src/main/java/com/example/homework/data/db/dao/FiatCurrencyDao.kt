package com.example.homework.data.db.dao

import androidx.room.*
import com.example.homework.data.db.entity.FiatCurrencyInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface FiatCurrencyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun multipleInserts(list: List<FiatCurrencyInfo>)

    @Query("DELETE FROM fiat_currency_info")
    fun deleteAll()

    @Query("SELECT * FROM fiat_currency_info ORDER BY ID ASC")
    fun getAll(): Flow<List<FiatCurrencyInfo>>
}