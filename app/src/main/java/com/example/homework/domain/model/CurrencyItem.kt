package com.example.homework.domain.model

sealed class CurrencyItem {
    data class CryptoCurrency(val id: String, val name: String, val symbol: String) : CurrencyItem()
    data class FiatCurrency(val id: String, val name: String, val symbol: String, val code: String) : CurrencyItem()
}