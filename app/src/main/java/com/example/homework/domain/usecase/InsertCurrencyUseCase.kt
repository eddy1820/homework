package com.example.homework.domain.usecase

import android.content.Context
import com.example.homework.data.db.entity.CryptoCurrencyInfo
import com.example.homework.data.db.entity.FiatCurrencyInfo
import com.example.homework.data.repository.CryptoCurrencyRepository
import com.example.homework.data.repository.FiatCurrencyRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InsertCurrencyUseCase @Inject constructor(
    @ApplicationContext val context: Context,
    private val cryptoCurrencyRepository: CryptoCurrencyRepository,
    private val fiatCurrencyRepository: FiatCurrencyRepository
) {
    companion object {
        const val CRYPTO_FILE_NAME = "crypto_currency.json"
        const val FIAT_FILE_NAME = "fiat_currency.json"
    }

    suspend operator fun invoke() {
        val cryptoJson = readJsonFromAssets(context, CRYPTO_FILE_NAME)
        val cryptoCurrencies = parseCurrencyList<CryptoCurrencyInfo>(cryptoJson)
        val fiatJson = readJsonFromAssets(context, FIAT_FILE_NAME)
        val fiatCurrencies = parseCurrencyList<FiatCurrencyInfo>(fiatJson)
        cryptoCurrencyRepository.multipleInserts(cryptoCurrencies)
        fiatCurrencyRepository.multipleInserts(fiatCurrencies)
    }

    fun readJsonFromAssets(context: Context, filename: String): String {
        return context.assets.open(filename).bufferedReader().use { it.readText() }
    }

    inline fun <reified T> parseCurrencyList(json: String): List<T> {
        val gson = Gson()
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }

}