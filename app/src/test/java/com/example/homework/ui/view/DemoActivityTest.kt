package com.example.homework.ui.view

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.homework.ui.viewmodel.CurrencyListViewModel

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.example.homework.R
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
class DemoActivityTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<DemoActivity>()

    @MockK(relaxed = true)
    private lateinit var viewModel: CurrencyListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        composeTestRule.activity.runOnUiThread {
            composeTestRule.activity.setContent {
                MainActivityLayout(viewModel = viewModel)
            }
        }
    }

    @Test
    fun testButtonsAreDisplayed() {
        val buttonTexts = listOf(
            R.string.clear, R.string.insert, R.string.crypto, R.string.fiat, R.string.all
        )
        buttonTexts.forEach { buttonText ->
            composeTestRule.onNodeWithText(composeTestRule.activity.getString(buttonText))
                .assertIsDisplayed()
        }
    }

    @Test
    fun testClearButtonClick() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.clear))
            .performClick()
        verify { viewModel.clearData() }
    }

    @Test
    fun testInsertButtonClick() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.insert))
            .performClick()
        verify { viewModel.insertAllData() }
    }

    @Test
    fun testCryptoButtonClick() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.crypto))
            .performClick()
        verify { viewModel.fetchCurrencies(includeCrypto = true, includeFiat = false) }
    }

    @Test
    fun testFiatButtonClick() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.fiat))
            .performClick()
        verify { viewModel.fetchCurrencies(includeCrypto = false, includeFiat = true) }
    }

    @Test
    fun testAllButtonClick() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.all))
            .performClick()
        verify { viewModel.fetchCurrencies(includeCrypto = true, includeFiat = true) }
    }
}