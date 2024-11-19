package com.example.homework.data.repository


import com.example.homework.data.db.dao.FiatCurrencyDao
import com.example.homework.data.db.entity.FiatCurrencyInfo
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

class FiatCurrencyRepositoryTest {

    @MockK(relaxed = true)
    lateinit var fiatCurrencyDao: FiatCurrencyDao

    private lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        fiatCurrencyRepository = FiatCurrencyRepository(fiatCurrencyDao)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun testGetAll() = runBlocking {
        val fiatCurrencyList = listOf(
            FiatCurrencyInfo(
                id = "SGD",
                name = "Singapore Dollar",
                symbol = "\$",
                code = "SGD"
            )
        )
        coEvery { fiatCurrencyDao.getAll() } returns flowOf(fiatCurrencyList)

        val result = fiatCurrencyRepository.getAll().first()
        assertEquals(1, result.size)
        assertEquals(fiatCurrencyList[0], result[0])
    }

    @Test
    fun testMultipleInserts() = runBlocking {
        val fiatCurrency =
            FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD")

        fiatCurrencyRepository.multipleInserts(listOf(fiatCurrency))
        coVerify { fiatCurrencyDao.multipleInserts(listOf(fiatCurrency)) }
    }

    @Test
    fun testDeleteAll() = runBlocking {
        fiatCurrencyRepository.deleteAll()
        coVerify { fiatCurrencyDao.deleteAll() }
    }
}