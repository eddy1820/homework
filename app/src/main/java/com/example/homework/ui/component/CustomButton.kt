package com.example.homework.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.homework.ui.theme.Grey400
import com.example.homework.ui.theme.Grey800


@Composable
fun CustomButton(
    modifier: Modifier,
    @StringRes text: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.padding(2.dp),
        contentPadding = PaddingValues(2.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Grey400
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            maxLines = 1,
            text = stringResource(text),
            color = Grey800
        )
    }
}