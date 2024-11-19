package com.example.homework.domain.usecase

import com.example.homework.data.db.entity.FiatCurrencyInfo
import com.example.homework.data.repository.FiatCurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetFiatCurrenciesUseCase @Inject constructor(
    private val repository: FiatCurrencyRepository
) {
    operator fun invoke(): Flow<List<FiatCurrencyInfo>> {
        return repository.getAll().flowOn(Dispatchers.IO)
    }
}