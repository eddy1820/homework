package com.example.homework.domain.usecase

import com.example.homework.data.repository.CryptoCurrencyRepository
import com.example.homework.data.repository.FiatCurrencyRepository
import javax.inject.Inject

class ClearCurrencyDataUseCase @Inject constructor(
    private val cryptoRepository: CryptoCurrencyRepository,
    private val fiatRepository: FiatCurrencyRepository
) {
    suspend operator fun invoke() {
        cryptoRepository.deleteAll()
        fiatRepository.deleteAll()
    }
}