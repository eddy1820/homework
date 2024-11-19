package com.example.homework.domain.usecase

import android.content.Context
import com.example.homework.data.repository.FiatCurrencyRepository
import com.example.homework.data.repository.CryptoCurrencyRepository
import com.example.homework.data.db.entity.FiatCurrencyInfo
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream

class InsertCurrencyUseCaseTest {


    @MockK
    private lateinit var context: Context

    @MockK
    private lateinit var cryptoCurrencyRepository: CryptoCurrencyRepository

    @MockK
    private lateinit var fiatCurrencyRepository: FiatCurrencyRepository

    private lateinit var useCase: InsertCurrencyUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = InsertCurrencyUseCase(context, cryptoCurrencyRepository, fiatCurrencyRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun testReadJsonFromAssets() {
        val fakeJson = """[{"id": "B", "name": "Bitcoin", "symbol": "BTC"}]"""
        val mockInputStream = ByteArrayInputStream(fakeJson.toByteArray())

        every { context.assets.open("crypto_currency.json") } returns mockInputStream

        val result = useCase.readJsonFromAssets(context, "crypto_currency.json")

        assertEquals(fakeJson, result)
    }

    @Test
    fun testParseCurrencyList() {
        val json = """[{"id": "SGD", "name": "Singapore Dollar", "symbol": "$", "code": "SGD"}]"""

        val result = useCase.parseCurrencyList<CryptoCurrencyInfo>(json)

        assertEquals(1, result.size)
        assertEquals("SGD", result[0].id)
        assertEquals("Singapore Dollar", result[0].name)
        assertEquals("$", result[0].symbol)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testInsertCurrencyUseCaseInvoke() = runTest {
        val fakeCryptoJson = """[{"id": "B", "name": "Bitcoin", "symbol": "BTC"}]"""
        val fakeFiatJson = """[{"id": "SGD", "name": "Singapore Dollar", "symbol": "$", "code": "SGD"}]"""
        val cryptoList = listOf(CryptoCurrencyInfo(id = "B", name = "Bitcoin", symbol = "BTC"))
        val fiatList = listOf(FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD"))

        every { context.assets.open("crypto_currency.json") } returns ByteArrayInputStream(fakeCryptoJson.toByteArray())
        every { context.assets.open("fiat_currency.json") } returns ByteArrayInputStream(fakeFiatJson.toByteArray())
        coEvery { cryptoCurrencyRepository.multipleInserts(any()) } just Runs
        coEvery { fiatCurrencyRepository.multipleInserts(any()) } just Runs

        useCase.invoke()

        coVerify { cryptoCurrencyRepository.multipleInserts(cryptoList) }
        coVerify { fiatCurrencyRepository.multipleInserts(fiatList) }
    }
}