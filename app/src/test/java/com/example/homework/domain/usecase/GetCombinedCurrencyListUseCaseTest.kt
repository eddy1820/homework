package com.example.homework.domain.usecase

import com.example.homework.data.db.entity.CryptoCurrencyInfo
import com.example.homework.data.db.entity.FiatCurrencyInfo
import com.example.homework.domain.model.CurrencyItem
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetCombinedCurrencyListUseCaseTest {

    @MockK
    lateinit var getCryptoCurrenciesUseCase: GetCryptoCurrenciesUseCase

    @MockK
    lateinit var getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase

    private lateinit var getCombinedCurrencyListUseCase: GetCombinedCurrencyListUseCase

    lateinit var cryptoCurrencyList: List<CryptoCurrencyInfo>
    lateinit var fiatCurrencyList: List<FiatCurrencyInfo>

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getCombinedCurrencyListUseCase =
            GetCombinedCurrencyListUseCase(getCryptoCurrenciesUseCase, getFiatCurrenciesUseCase)
        cryptoCurrencyList = listOf(
            CryptoCurrencyInfo(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CryptoCurrencyInfo(id = "ETH", name = "Ethereum", symbol = "ETH")
        )
        fiatCurrencyList = listOf(
            FiatCurrencyInfo(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD"),
            FiatCurrencyInfo(id = "EUR", name = "Euro", symbol = "€", code = "EUR")
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testGetCombinedCurrencyListUseCaseInvokeWithBothIncluded() = runBlocking {
        val cryptoCurrencyInfoList = cryptoCurrencyList.map {
            CurrencyItem.CryptoCurrency(
                id = it.id,
                name = it.name,
                symbol = it.symbol
            )
        }

        val fiatCurrencyInfoList = fiatCurrencyList.map {
            CurrencyItem.FiatCurrency(
                id = it.id,
                name = it.name,
                symbol = it.symbol,
                code = it.code
            )
        }

        coEvery { getCryptoCurrenciesUseCase() } returns flowOf(cryptoCurrencyList)
        coEvery { getFiatCurrenciesUseCase() } returns flowOf(fiatCurrencyList)

        val result = getCombinedCurrencyListUseCase.invoke(true, true).first()

        assertEquals(4, result.size)
        assertEquals(result, cryptoCurrencyInfoList + fiatCurrencyInfoList)
    }

    @Test
    fun testGetCombinedCurrencyListUseCaseInvokeWithFilter() = runBlocking {
        coEvery { getFiatCurrenciesUseCase() } returns flowOf(fiatCurrencyList)
        coEvery { getCryptoCurrenciesUseCase() } returns flowOf(cryptoCurrencyList)

        val result = getCombinedCurrencyListUseCase.invoke(true, true, "Bit").first()
        assertEquals(1, result.size)
        assertTrue(
            result.contains(
                CurrencyItem.CryptoCurrency(
                    id = "BTC",
                    name = "Bitcoin",
                    symbol = "BTC"
                )
            )
        )

        val result2 = getCombinedCurrencyListUseCase.invoke(true, true, "ETH").first()
        assertEquals(1, result2.size)
        assertTrue(
            result2.contains(
                CurrencyItem.CryptoCurrency(
                    id = "ETH",
                    name = "Ethereum",
                    symbol = "ETH"
                )
            )
        )

        val result3 = getCombinedCurrencyListUseCase.invoke(true, true, "Dollar").first()
        assertEquals(1, result3.size)
        assertTrue(
            result3.contains(
                CurrencyItem.FiatCurrency(
                    id = "SGD",
                    name = "Singapore Dollar",
                    symbol = "$",
                    code = "SGD"
                )
            )
        )

        val result4 = getCombinedCurrencyListUseCase.invoke(true, true, "XYZ").first()
        assertEquals(0, result4.size)
    }

    @Test
    fun testGetCombinedCurrencyListUseCaseInvokeWithOnlyFiatIncluded() = runBlocking {

        coEvery { getFiatCurrenciesUseCase() } returns flowOf(fiatCurrencyList)
        coEvery { getCryptoCurrenciesUseCase() } returns flowOf(emptyList())

        val result = getCombinedCurrencyListUseCase.invoke(false, true).first()
        assertEquals(2, result.size)
        assertEquals(
            CurrencyItem.FiatCurrency(
                id = "SGD",
                name = "Singapore Dollar",
                symbol = "$",
                code = "SGD"
            ), result[0]
        )

        assertEquals(
            CurrencyItem.FiatCurrency(id = "EUR", name = "Euro", symbol = "€", code = "EUR"),
            result[1]
        )
    }

    @Test
    fun testGetCombinedCurrencyListUseCaseInvokeWithOnlyCryptoIncluded() = runBlocking {
        coEvery { getFiatCurrenciesUseCase() } returns flowOf(emptyList())
        coEvery { getCryptoCurrenciesUseCase() } returns flowOf(cryptoCurrencyList)

        val result = getCombinedCurrencyListUseCase.invoke(true, false).first()
        assertEquals(2, result.size)
        assertEquals(
            CurrencyItem.CryptoCurrency(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            result[0]
        )
        assertEquals(
            CurrencyItem.CryptoCurrency(id = "ETH", name = "Ethereum", symbol = "ETH"),
            result[1]
        )
    }
}