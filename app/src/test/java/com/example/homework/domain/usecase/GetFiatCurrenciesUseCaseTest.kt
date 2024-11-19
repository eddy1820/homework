package com.example.homework.domain.usecase

import com.example.homework.data.repository.FiatCurrencyRepository
import com.example.homework.data.db.entity.FiatCurrencyInfo
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

class GetFiatCurrenciesUseCaseTest {

    @MockK
    lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    private lateinit var getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getFiatCurrenciesUseCase = GetFiatCurrenciesUseCase(fiatCurrencyRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testGetFiatCurrenciesUseCaseInvoke() = runBlocking {
        val fiatCurrencyList = listOf(
            FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD"),
            FiatCurrencyInfo(id = "EUR", name = "Euro", symbol = "€", code = "EUR")
        )
        coEvery { fiatCurrencyRepository.getAll() } returns flowOf(fiatCurrencyList)

        val result = getFiatCurrenciesUseCase().first()

        assertEquals(2, result.size)
        assertEquals(fiatCurrencyList[0], result[0])
        assertEquals(fiatCurrencyList[1], result[1])
    }
}