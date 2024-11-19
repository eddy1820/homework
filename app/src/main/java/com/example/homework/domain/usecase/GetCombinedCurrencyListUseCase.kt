package com.example.homework.domain.usecase

import android.util.Log
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import com.example.homework.data.db.entity.FiatCurrencyInfo
import com.example.homework.domain.model.CurrencyItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCombinedCurrencyListUseCase @Inject constructor(
    private val getCryptoCurrenciesUseCase: GetCryptoCurrenciesUseCase,
    private val getFiatCurrenciesUseCase: GetFiatCurrenciesUseCase
) {
    suspend operator fun invoke(
        includeCrypto: Boolean,
        includeFiat: Boolean,
        filter: String = ""
    ): Flow<List<CurrencyItem>> {
        val cryptoFlow = if (includeCrypto) getCryptoCurrenciesUseCase() else flowOf(emptyList())
        val fiatFlow = if (includeFiat) getFiatCurrenciesUseCase() else flowOf(emptyList())

        return combine(
            cryptoFlow,
            fiatFlow
        ) { cryptoList: List<CryptoCurrencyInfo>, fiatList: List<FiatCurrencyInfo> ->
            val cryptoItems: List<CurrencyItem.CryptoCurrency> = cryptoList.filter {
                filterCurrency(it.name, it.symbol, filter)
            }.map {
                CurrencyItem.CryptoCurrency(id = it.id, name = it.name, symbol = it.symbol)
            }
            val fiatItems: List<CurrencyItem.FiatCurrency> = fiatList.filter {
                filterCurrency(it.name, "", filter)
            }.map {
                CurrencyItem.FiatCurrency(
                    id = it.id, name = it.name, symbol = it.symbol, code = it.code
                )
            }
            return@combine cryptoItems + fiatItems
        }.flowOn(Dispatchers.IO)
    }

    private fun filterCurrency(
        name: String,
        symbol: String,
        filter: String = ""
    ): Boolean {
        if (filter.isEmpty()) return true
        return listOf(
            // The coin’s name (e.g. Bitcoin) starts with the search term
            name.startsWith(filter, ignoreCase = true),
            // The coin’s name contains a partial match with a ‘ ’ (space) prefixed to the search term
            name.contains(" $filter", ignoreCase = true),
            // The coin’s symbol starts with the search term
            symbol.startsWith(filter, ignoreCase = true)
        ).any { it }
    }



}