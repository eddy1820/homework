package com.example.homework.data.repository

import com.example.homework.data.db.dao.CryptoCurrencyDao
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CryptoCurrencyRepositoryTest {

    @MockK(relaxed = true)
    lateinit var cryptoCurrencyDao: CryptoCurrencyDao

    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cryptoCurrencyRepository = CryptoCurrencyRepository(cryptoCurrencyDao)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGetAll() = runBlocking {
        val cryptoCurrencyList =
            listOf(CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC"))
        coEvery { cryptoCurrencyDao.getAll() } returns flowOf(cryptoCurrencyList)
        val result = cryptoCurrencyRepository.getAll().first()
        assertEquals(1, result.size)
        assertEquals(cryptoCurrencyList[0], result[0])
    }

    @Test
    fun testMultipleInserts() = runBlocking {
        val cryptoCurrency = CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC")
        cryptoCurrencyRepository.multipleInserts(listOf(cryptoCurrency))
        coVerify { cryptoCurrencyDao.multipleInserts(listOf(cryptoCurrency)) }
    }

    @Test
    fun testDeleteAll() = runBlocking {
        cryptoCurrencyRepository.deleteAll()
        coVerify { cryptoCurrencyDao.deleteAll() }
    }
}