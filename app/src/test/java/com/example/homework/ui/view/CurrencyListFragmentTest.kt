package com.example.homework.ui.view


import android.content.Context
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.core.app.ApplicationProvider
import com.example.homework.domain.model.CurrencyItem
import com.example.homework.ui.viewmodel.CurrencyListViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.example.homework.R

@RunWith(RobolectricTestRunner::class)
class CurrencyListFragmentTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private lateinit var context: Context

    @MockK(relaxed = true)
    private lateinit var viewModel: CurrencyListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun testWhenListIsEmptyAndShowsEmptyList() {
        every { viewModel.currencyList } returns MutableStateFlow(emptyList())
        every { viewModel.searchText } returns MutableStateFlow("")
        composeTestRule.setContent { CurrencyListLayout(viewModel = viewModel) }
        composeTestRule.onNodeWithText(context.getString(R.string.no_result)).assertDoesNotExist()
        composeTestRule.onNodeWithText(context.getString(R.string.try_mco)).assertDoesNotExist()
    }

    @Test
    fun testWhenSearchHasNoResultsAndShowsNoResultView() {
        every { viewModel.currencyList } returns MutableStateFlow(emptyList())
        every { viewModel.searchText } returns MutableStateFlow("xxxxxx")
        composeTestRule.setContent { CurrencyListLayout(viewModel = viewModel) }
        composeTestRule.onNodeWithText(context.getString(R.string.no_result)).assertExists()
        composeTestRule.onNodeWithText(context.getString(R.string.try_mco)).assertExists()
    }

    @Test
    fun testShowsAllItem() {
        val currencyList = listOf(
            CurrencyItem.CryptoCurrency(
                id = "ETH",
                name = "Ethereum",
                symbol = "ETH"
            ),
            CurrencyItem.FiatCurrency(
                id = "SGD",
                name = "Singapore Dollar",
                symbol = "$",
                code = "SGD"
            )
        )

        every { viewModel.currencyList } returns MutableStateFlow(currencyList)
        every { viewModel.searchText } returns MutableStateFlow("")

        composeTestRule.setContent {
            CurrencyListLayout(viewModel = viewModel)
        }
        currencyList.forEach {
            when(it) {
                is CurrencyItem.CryptoCurrency -> {
                    composeTestRule.onNodeWithText(it.id.getOrNull(0)?.toString()?:"").assertExists()
                    composeTestRule.onNodeWithText(it.name).assertExists()
                }
                is CurrencyItem.FiatCurrency -> {
                    composeTestRule.onNodeWithText(it.id.getOrNull(0)?.toString()?:"").assertExists()
                    composeTestRule.onNodeWithText(it.name).assertExists()
                }
            }
        }
    }

    @Test
    fun testWhenSearchingCallsViewModelWithSearchText() {
        composeTestRule.setContent {
            CurrencyListLayout(viewModel = viewModel)
        }
        composeTestRule.onNode(hasSetTextAction()).performTextInput("BTC")
        verify { viewModel.fetchCurrencies(searchText = "BTC") }
    }

    @Test
    fun testWhenClickingClearButtonClearsSearchAndRefreshes() {
        every { viewModel.searchText } returns MutableStateFlow("BTC")
        composeTestRule.setContent {
            CurrencyListLayout(viewModel = viewModel)
        }
        composeTestRule.onNode(hasTestTag("Close")).performClick()
        verify { viewModel.fetchCurrencies(searchText = "") }
    }

    @Test
    fun testWhenClickingBackButtonClearsSearchAndRefreshes() {
        every { viewModel.searchText } returns MutableStateFlow("BTC")
        composeTestRule.setContent {
            CurrencyListLayout(viewModel = viewModel)
        }
        composeTestRule.onNode(hasTestTag("Back")).performClick()
        verify { viewModel.fetchCurrencies(searchText = "") }
    }
}