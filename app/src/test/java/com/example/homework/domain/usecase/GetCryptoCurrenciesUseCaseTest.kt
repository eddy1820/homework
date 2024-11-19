package com.example.homework.domain.usecase

import com.example.homework.data.repository.CryptoCurrencyRepository
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetCryptoCurrenciesUseCaseTest {

    @MockK
    lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    private lateinit var getCryptoCurrenciesUseCase: GetCryptoCurrenciesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCryptoCurrenciesUseCase = GetCryptoCurrenciesUseCase(cryptoCurrencyRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testGetCryptoCurrenciesUseCaseInvoke() = runBlocking {
        val cryptoCurrencyList = listOf(
            CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC"),
            CryptoCurrencyInfo(id = "E", name = "Ethereum", symbol = "ETH")
        )
        coEvery { cryptoCurrencyRepository.getAll() } returns flowOf(cryptoCurrencyList)

        val result = getCryptoCurrenciesUseCase().first()

        assertEquals(2, result.size)
        assertEquals(cryptoCurrencyList[0], result[0])
        assertEquals(cryptoCurrencyList[1], result[1])
    }
}