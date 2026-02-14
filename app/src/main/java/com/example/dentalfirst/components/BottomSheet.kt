package com.example.dentalfirst.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalfirst.R
import com.example.dentalfirst.ui.theme.DarkGrey
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.Orange
import com.example.dentalfirst.ui.theme.Orange_10
import com.example.dentalfirst.ui.theme.SuperLightGrey
import com.example.dentalfirst.ui.theme.TooLightGrey
import com.example.dentalfirst.utils.toPriceString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    show: Boolean,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    if (show) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss.invoke() },
            sheetState = sheetState,
            modifier = modifier,
            shape = MaterialTheme.shapes.large,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 0.dp,
                        bottom = 12.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                content = content
            )
        }
    }
}

@Composable
fun DeliveryBottomSheetText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Normal,
            lineHeight = 21.sp
        ),
        color = DarkGrey,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun DeliveryBottomSheet(
    show: Boolean,
    addAmountToFree: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BottomSheet(
        show,
        onDismiss,
        modifier
    ) {
        Image(
            ImageVector.vectorResource(R.drawable.danger_ic),
            contentDescription = null
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "Доставка до ТК",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        DeliveryBottomSheetText("Стоимость доставки до транспортной компании - 1 500 ₽")
        Spacer(Modifier.height(8.dp))
        DeliveryBottomSheetText("Доставка транспортной компанией рассчитывается отдельно")
        Spacer(Modifier.height(8.dp))
        Surface(
            color = SuperLightGrey,
            border = BorderStroke(
                1.dp,
                TooLightGrey
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = buildAnnotatedString {
                    append("Дополните свой заказ ещё на ")

                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.secondary
                        )
                    ) {
                        append(addAmountToFree.toPriceString())
                    }

                    append(", чтобы доставка стала бесплатной")
                },
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal,
                    lineHeight = 21.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(20.dp))

        Button(onClick = { onDismiss.invoke() },
            contentPadding = PaddingValues(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange_10,
                contentColor = Orange
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Понятно",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight =
                        FontWeight.Bold,
                    fontSize = 17.sp,
                )
            )
        }
    }
}

@Preview
@Composable
private fun DeliveryBottomSheetPreview() {
    DentalFirstTheme() {
        DeliveryBottomSheet(
            true,
            35000,
            {})
    }

}