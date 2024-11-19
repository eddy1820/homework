package com.example.homework.ui.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.homework.R
import com.example.homework.ui.theme.Grey200
import com.example.homework.ui.theme.Grey300
import com.example.homework.ui.theme.Grey500
import com.example.homework.ui.theme.Grey800

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Grey200),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.testTag("Back"),
                onClick = onBackClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    tint = Grey800
                )
            }

            TextField(
                value = searchText,
                onValueChange = onSearchTextChanged,
                placeholder = { Text(text = "Search", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(color = Grey200),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Grey500,
                )
            )

            IconButton(
                modifier = Modifier.testTag("Close"),
                onClick = onCloseClick
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "Close",
                    tint = Grey800
                )
            }
        }
        HorizontalDivider(thickness = 1.dp, color = Grey300)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SearchBar(
        searchText = "",
        onSearchTextChanged = { },
        onBackClick = { },
        onCloseClick = { }
    )
}