package com.example.homework.domain.usecase

import com.example.homework.data.repository.FiatCurrencyRepository
import com.example.homework.data.repository.CryptoCurrencyRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class ClearCurrencyDataUseCaseTest {

    @MockK(relaxed = true)
    lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    @MockK(relaxed = true)
    lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    private lateinit var clearCurrencyDataUseCase: ClearCurrencyDataUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        clearCurrencyDataUseCase = ClearCurrencyDataUseCase(cryptoCurrencyRepository,fiatCurrencyRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testClearsDataInvoke() = runBlocking {
        coEvery { fiatCurrencyRepository.deleteAll() } returns Unit
        coEvery { cryptoCurrencyRepository.deleteAll() } returns Unit

        clearCurrencyDataUseCase()

        coVerify { fiatCurrencyRepository.deleteAll() }
        coVerify { cryptoCurrencyRepository.deleteAll() }
    }
}