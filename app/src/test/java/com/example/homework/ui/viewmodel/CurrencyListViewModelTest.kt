package com.example.homework.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.example.homework.domain.model.CurrencyItem
import com.example.homework.domain.usecase.ClearCurrencyDataUseCase
import com.example.homework.domain.usecase.GetCombinedCurrencyListUseCase
import com.example.homework.domain.usecase.InsertCurrencyUseCase
import io.mockk.*
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@OptIn(ExperimentalCoroutinesApi::class)
class CurrencyListViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @MockK(relaxed = true)
    private lateinit var insertCurrencyUseCase: InsertCurrencyUseCase

    @MockK(relaxed = true)
    private lateinit var getCombinedCurrencyListUseCase: GetCombinedCurrencyListUseCase

    @MockK(relaxed = true)
    private lateinit var clearCurrencyDataUseCase: ClearCurrencyDataUseCase

    private lateinit var viewModel: CurrencyListViewModel


    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        MockKAnnotations.init(this)
        viewModel = CurrencyListViewModel(insertCurrencyUseCase, getCombinedCurrencyListUseCase, clearCurrencyDataUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun testFetchCurrenciesShouldUpdateCurrencyListCorrectly() = runTest {
        val fakeCurrencyList = listOf(
            CurrencyItem.CryptoCurrency(id = "BTC", name = "Bitcoin", symbol = "BTC"),
            CurrencyItem.FiatCurrency(id = "SGD", name = "Singapore Dollar", symbol = "$", code = "SGD")
        )
        val flow = flowOf(fakeCurrencyList)
        coEvery { getCombinedCurrencyListUseCase(true, true, "") } returns flow
        viewModel.fetchCurrencies()
        advanceUntilIdle()
        assertEquals(fakeCurrencyList, viewModel.currencyList.value)
    }

    @Test
    fun testWhenSearchTextChangedThenFetchCurrenciesIsCalledAfterDebounce() = runTest {
        viewModel.searchText.test {
            assertEquals("", awaitItem())
            viewModel.searchTextChanged("BTC")
            assertEquals("BTC", awaitItem())
            advanceTimeBy(400)
            coVerify {
                getCombinedCurrencyListUseCase(true, true, "BTC")
            }
        }
    }

    @Test
    fun testInsertAllDataShouldCallInsertCurrencyUseCaseAndResetState() = runTest {
        coEvery { insertCurrencyUseCase() } just Runs
        coEvery { getCombinedCurrencyListUseCase(true, true, "") } returns flowOf(emptyList())

        viewModel.insertAllData()
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { insertCurrencyUseCase() }
        assertEquals(emptyList<CurrencyItem>(), viewModel.currencyList.value)
        assertEquals("", viewModel.searchText.value)
    }

    @Test
    fun testClearDataShouldCallClearCurrencyDataUseCaseAndResetState() = runTest {
        coEvery { clearCurrencyDataUseCase() } just Runs
        viewModel.clearData()
        coVerify { clearCurrencyDataUseCase() }
        assertEquals(emptyList<CurrencyItem>(), viewModel.currencyList.value)
        assertEquals("", viewModel.searchText.value)
    }
}