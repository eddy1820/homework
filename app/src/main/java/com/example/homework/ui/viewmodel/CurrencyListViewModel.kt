package com.example.homework.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework.domain.model.CurrencyItem
import com.example.homework.domain.usecase.ClearCurrencyDataUseCase
import com.example.homework.domain.usecase.GetCombinedCurrencyListUseCase
import com.example.homework.domain.usecase.InsertCurrencyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val insertCurrencyUseCase: InsertCurrencyUseCase,
    private val getCombinedCurrencyListUseCase: GetCombinedCurrencyListUseCase,
    private val clearCurrencyDataUseCase: ClearCurrencyDataUseCase
) : ViewModel() {
    private val _currencyList = MutableStateFlow<List<CurrencyItem>>(emptyList())
    val currencyList: StateFlow<List<CurrencyItem>> = _currencyList
    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private var includeCrypto = true
    private var includeFiat = true
    private var job: Job? = null

    fun fetchCurrencies(
        includeCrypto: Boolean = this.includeCrypto,
        includeFiat: Boolean = this.includeFiat,
        searchText: String = ""
    ) {
        this.includeCrypto = includeCrypto
        this.includeFiat = includeFiat
        _searchText.value = searchText
        job?.cancel()
        job = viewModelScope.launch {
            getCombinedCurrencyListUseCase(includeCrypto, includeFiat, searchText)
                .catch {
                    _currencyList.value = emptyList()
                }
                .collect { combinedList ->
                    _currencyList.value = combinedList
                }
        }

    }

    fun insertAllData() = viewModelScope.launch(Dispatchers.IO) {
        resetState()
        insertCurrencyUseCase()
        fetchCurrencies()
    }

    fun clearData() = viewModelScope.launch(Dispatchers.IO) {
        resetState()
        clearCurrencyDataUseCase()
    }

    private fun resetState() {
        includeFiat = true
        includeCrypto = true
        job?.cancel()
        _currencyList.value = emptyList()
        _searchText.value = ""
    }
}