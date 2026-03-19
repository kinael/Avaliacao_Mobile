package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    private val viewModel: TipViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                TipCalculatorScreen(viewModel)
            }
        }
    }
}

@Composable
fun TipCalculatorScreen(viewModel: TipViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F3F3))
    ) {
        TopBar()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp)
        ) {
            AmountSection(amount = state.formattedAmount)

            Spacer(modifier = Modifier.height(8.dp))

            CustomTipSection(
                customTipPercent = state.customTipPercent,
                onPercentChanged = { viewModel.updateCustomPercent(it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            ResultsTable(
                fixedTipLabel = "15%",
                customTipLabel = "${state.customTipPercent.toInt()}%",
                fixedTip = state.formattedFixedTip,
                customTip = state.formattedCustomTip,
                fixedTotal = state.formattedFixedTotal,
                customTotal = state.formattedCustomTotal
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        NumericKeyboard(
            onDigitClick = { digit -> viewModel.onDigitPressed(digit) },
            onDeleteClick = { viewModel.onDeletePressed() }
        )
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF202020))
            .padding(horizontal = 10.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Info",
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "Tip Calculator",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun AmountSection(amount: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Amount",
            modifier = Modifier.width(62.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(34.dp)
                .background(Color(0xFFE6E6E6))
                .border(1.dp, Color(0xFFD0D0D0)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = amount,
                modifier = Modifier.padding(start = 8.dp),
                color = Color.Black,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun CustomTipSection(
    customTipPercent: Float,
    onPercentChanged: (Float) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(62.dp)
        ) {
            Text(
                text = "Custom",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
            Text(
                text = "%",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.DarkGray
            )
        }

        Slider(
            value = customTipPercent,
            onValueChange = { onPercentChanged(it) },
            valueRange = 0f..30f,
            modifier = Modifier.weight(1f),
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF9E9E9E), // bolinha
                activeTrackColor = Color(0xFFBDBDBD),
                inactiveTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}

@Composable
fun ResultsTable(
    fixedTipLabel: String,
    customTipLabel: String,
    fixedTip: String,
    customTip: String,
    fixedTotal: String,
    customTotal: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(62.dp))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = fixedTipLabel,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = customTipLabel,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        ResultRow(
            label = "Tip",
            leftValue = fixedTip,
            rightValue = customTip
        )

        Spacer(modifier = Modifier.height(6.dp))

        ResultRow(
            label = "Total",
            leftValue = fixedTotal,
            rightValue = customTotal
        )
    }
}

@Composable
fun ResultRow(
    label: String,
    leftValue: String,
    rightValue: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.width(62.dp),
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(32.dp)
                .background(Color(0xFFE6E6E6)) // cinza
                .border(1.dp, Color(0xFFBDBDBD)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = leftValue,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .height(32.dp)
                .background(Color(0xFFE6E6E6))
                .border(1.dp, Color(0xFFD0D0D0)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = rightValue,
                color = Color.Black
            )
        }
    }
}

@Composable
fun NumericKeyboard(
    onDigitClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF353535))
    ) {
        KeyboardRow(
            keys = listOf("1", "2", "3"),
            onKeyClick = { key -> onDigitClick(key.toInt()) }
        )

        KeyboardRow(
            keys = listOf("4", "5", "6"),
            onKeyClick = { key -> onDigitClick(key.toInt()) }
        )

        KeyboardRow(
            keys = listOf("7", "8", "9"),
            onKeyClick = { key -> onDigitClick(key.toInt()) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(58.dp)
                    .background(Color(0xFF353535))
                    .border(1.dp, Color(0xFF4B4B4B))
            )

            KeyboardButton(
                text = "0",
                onClick = { onDigitClick(0) },
                modifier = Modifier.weight(1f)
            )

            KeyboardButton(
                text = "⌫",
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeyboardRow(
    keys: List<String>,
    onKeyClick: (String) -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        keys.forEach { key ->
            KeyboardButton(
                text = key,
                onClick = { onKeyClick(key) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun KeyboardButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(58.dp)
            .background(Color(0xFF3C3C3C))
            .border(1.dp, Color(0xFF565656))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
