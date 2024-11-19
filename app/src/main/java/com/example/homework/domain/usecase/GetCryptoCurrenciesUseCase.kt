package com.example.homework.domain.usecase

import com.example.homework.data.db.entity.CryptoCurrencyInfo
import com.example.homework.data.repository.CryptoCurrencyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCryptoCurrenciesUseCase @Inject constructor(
    private val repository: CryptoCurrencyRepository
) {
    operator fun invoke(): Flow<List<CryptoCurrencyInfo>> {
        return repository.getAll().flowOn(Dispatchers.IO)
    }
}