package com.example.homework.db.dto

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.homework.data.db.AppDatabase
import com.example.homework.data.db.dao.FiatCurrencyDao
import com.example.homework.data.db.entity.FiatCurrencyInfo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FiatCurrencyDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var fiatCurrencyDao: FiatCurrencyDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        fiatCurrencyDao = database.getFiatCurrencyDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testMultipleInsertsAndGetAll() = runBlocking {
        val fiatCurrency = FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD")
        fiatCurrencyDao.multipleInserts(listOf(fiatCurrency))

        val result = fiatCurrencyDao.getAll().first()
        assertEquals(1, result.size)
        assertEquals(fiatCurrency, result[0])
    }

    @Test
    fun testDeleteAll() = runBlocking {
        val fiatCurrency1 = FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD")
        val fiatCurrency2 = FiatCurrencyInfo(id = "EUR", name = "Euro", symbol = "€", code = "EUR")
        fiatCurrencyDao.multipleInserts(listOf(fiatCurrency1, fiatCurrency2))

        fiatCurrencyDao.deleteAll()
        val result = fiatCurrencyDao.getAll().first()
        assertEquals(0, result.size)
    }
}