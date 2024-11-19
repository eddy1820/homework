package com.example.homework.ui.view

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.homework.R
import com.example.homework.ui.component.CustomButton
import com.example.homework.ui.theme.Grey200
import com.example.homework.ui.theme.Grey300
import com.example.homework.ui.theme.HomeworkTheme
import com.example.homework.ui.viewmodel.CurrencyListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DemoActivity : AppCompatActivity() {
    private val viewModel by viewModels<CurrencyListViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeworkTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainActivityLayout(
                        viewModel = viewModel, modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MainActivityLayout(viewModel: CurrencyListViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Grey200)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AndroidView(factory = { context ->
                val fragmentManager = (context as AppCompatActivity).supportFragmentManager
                val fragmentContainer = FrameLayout(context).apply {
                    id = View.generateViewId()
                }
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = CurrencyListFragment.newInstance()
                fragmentTransaction.add(fragmentContainer.id, fragment)
                fragmentTransaction.commit()
                fragmentContainer
            })
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Grey300),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CustomButton(
                modifier = Modifier.weight(1f),
                text = R.string.clear,
                onClick = {
                    viewModel.clearData()
                })
            CustomButton(
                modifier = Modifier.weight(1f),
                text = R.string.insert,
                onClick = {
                    viewModel.insertAllData()
                })
            CustomButton(
                modifier = Modifier.weight(1f),
                text = R.string.crypto,
                onClick = {
                    viewModel.fetchCurrencies(includeCrypto = true, includeFiat = false)
                })
            CustomButton(
                modifier = Modifier.weight(1f),
                text = R.string.fiat,
                onClick = {
                    viewModel.fetchCurrencies(includeCrypto = false, includeFiat = true)
                })
            CustomButton(
                modifier = Modifier.weight(1f),
                text = R.string.all,
                onClick = {
                    viewModel.fetchCurrencies(includeCrypto = true, includeFiat = true)
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HomeworkTheme {
//        MainActivityLayout(viewModel)
    }
}