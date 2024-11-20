package com.example.homework.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import com.example.homework.domain.model.CurrencyItem
import com.example.homework.ui.component.CryptoCurrencyItemView
import com.example.homework.ui.component.FiatCurrencyItemView
import com.example.homework.ui.component.SearchBar
import com.example.homework.ui.viewmodel.CurrencyListViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.homework.R
import com.example.homework.ui.theme.Grey600
import com.example.homework.ui.theme.Grey800

@AndroidEntryPoint
class CurrencyListFragment : Fragment() {
    companion object {
        @JvmStatic
        fun newInstance() =
            CurrencyListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    private val viewModel by activityViewModels<CurrencyListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            CurrencyListLayout(viewModel)
        }
        return composeView
    }
}

@Composable
fun CurrencyListLayout(viewModel: CurrencyListViewModel) {
    val list: List<CurrencyItem> by viewModel.currencyList.collectAsStateWithLifecycle(initialValue = listOf())
    val searchText: String by viewModel.searchText.collectAsStateWithLifecycle(initialValue = "")

    Column {
        SearchBar(
            searchText = searchText,
            onSearchTextChanged = {
                viewModel.fetchCurrencies(searchText = it)
            },
            onBackClick = {
                viewModel.fetchCurrencies(searchText = "")
            },
            onCloseClick = {
                viewModel.fetchCurrencies(searchText = "")
            }
        )
        Box {
            LazyColumn {
                items(list) { item ->
                    when (item) {
                        is CurrencyItem.CryptoCurrency -> {
                            CryptoCurrencyItemView(item)
                        }

                        is CurrencyItem.FiatCurrency -> {
                            FiatCurrencyItemView(item)
                        }
                    }
                }
            }

            if (searchText.isNotEmpty() && list.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    Image(
                        painter = painterResource(id = R.drawable.no_file),
                        contentDescription = "",
                        modifier = Modifier.size(120.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = stringResource(R.string.no_result), color = Grey800, fontSize = 14.sp)
                    Text(text = stringResource(R.string.try_mco), color = Grey600, fontSize = 16.sp)
                }
            }
        }

    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CurrencyListLayout()
//}

