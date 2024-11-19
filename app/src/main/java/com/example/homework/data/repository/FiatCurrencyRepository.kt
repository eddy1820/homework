package com.example.homework.data.repository

import com.example.homework.data.db.dao.FiatCurrencyDao
import com.example.homework.data.db.entity.FiatCurrencyInfo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiatCurrencyRepository @Inject constructor(private val fiatCurrencyDao: FiatCurrencyDao) {

    fun getAll(): Flow<List<FiatCurrencyInfo>> {
        return fiatCurrencyDao.getAll()
    }

    fun multipleInserts(data: List<FiatCurrencyInfo>) {
        return fiatCurrencyDao.multipleInserts(data)
    }

    fun deleteAll() {
        return fiatCurrencyDao.deleteAll()
    }
}