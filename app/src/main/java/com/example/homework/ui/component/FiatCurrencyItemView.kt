package com.example.homework.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.example.homework.domain.model.CurrencyItem
import com.example.homework.ui.theme.Grey300

@Composable
fun FiatCurrencyItemView(data: CurrencyItem.FiatCurrency) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.White)
    ) {
        val (symbolLabel, nameLabel, divider) = createRefs()
        Text(text = data.id.getOrNull(0)?.toString() ?: "",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .size(30.dp)
                .background(color = Color.Gray, shape = RoundedCornerShape(12.dp))
                .wrapContentSize(align = Alignment.Center)
                .constrainAs(ref = symbolLabel) {
                    start.linkTo(anchor = parent.start, margin = 10.dp)
                    top.linkTo(anchor = parent.top)
                    bottom.linkTo(anchor = parent.bottom)
                })

        Text(text = data.name,
            color = Color.Black,
            fontSize = 20.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .constrainAs(ref = nameLabel) {
                    start.linkTo(anchor = symbolLabel.end, margin = 10.dp)
                    end.linkTo(anchor = parent.end, margin = 10.dp)
                    top.linkTo(anchor = parent.top)
                    bottom.linkTo(anchor = parent.bottom)
                    width = fillToConstraints
                })

        HorizontalDivider(
            modifier = Modifier.constrainAs(ref = divider) {
                start.linkTo(anchor = nameLabel.start)
                end.linkTo(anchor = parent.end)
                bottom.linkTo(anchor = parent.bottom)
                width = fillToConstraints
            }, thickness = 1.dp, color = Grey300
        )
    }
}
