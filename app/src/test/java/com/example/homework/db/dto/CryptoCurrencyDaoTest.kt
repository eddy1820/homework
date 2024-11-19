package com.example.homework.db.dto

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.homework.data.db.AppDatabase
import com.example.homework.data.db.dao.CryptoCurrencyDao
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CryptoCurrencyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var cryptoCurrencyDao: CryptoCurrencyDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        cryptoCurrencyDao = database.getCryptoCurrencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testMultipleInsertsAndGetAll() = runBlocking {
        val cryptoCurrency = CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC")
        cryptoCurrencyDao.multipleInserts(listOf(cryptoCurrency))

        val result = cryptoCurrencyDao.getAll().first()
        assertEquals(1, result.size)
        assertEquals(cryptoCurrency, result[0])
    }

    @Test
    fun testDeleteAll() = runBlocking {
        val cryptoCurrency1 = CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC")
        val cryptoCurrency2 = CryptoCurrencyInfo(id = "E", name = "Ethereum", symbol = "ETH")
        cryptoCurrencyDao.multipleInserts(listOf(cryptoCurrency1, cryptoCurrency2))

        cryptoCurrencyDao.deleteAll()
        val result = cryptoCurrencyDao.getAll().first()
        assertEquals(0, result.size)
    }
}