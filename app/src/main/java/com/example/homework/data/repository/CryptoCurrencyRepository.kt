package com.example.homework.data.repository

import com.example.homework.data.db.dao.CryptoCurrencyDao
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoCurrencyRepository @Inject constructor(private val cryptoCurrencyDao: CryptoCurrencyDao) {

    fun getAll(): Flow<List<CryptoCurrencyInfo>> {
        return cryptoCurrencyDao.getAll()
    }

    fun multipleInserts(data: List<CryptoCurrencyInfo>) {
        return cryptoCurrencyDao.multipleInserts(data)
    }

    fun deleteAll() {
        return cryptoCurrencyDao.deleteAll()
    }
}