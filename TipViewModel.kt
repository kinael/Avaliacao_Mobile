package com.example.tipcalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

data class TipUiState(
    val rawDigits: String = "",
    val customTipPercent: Float = 18f,
    val formattedAmount: String = "$0.00",
    val formattedFixedTip: String = "$0.00",
    val formattedCustomTip: String = "$0.00",
    val formattedFixedTotal: String = "$0.00",
    val formattedCustomTotal: String = "$0.00"
)

class TipViewModel : ViewModel() {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US)

    private val _uiState = MutableStateFlow(TipUiState())
    val uiState: StateFlow<TipUiState> = _uiState.asStateFlow()

    init {
        recalculate()
    }

    fun onDigitPressed(digit: Int) {
        val current = _uiState.value.rawDigits

        if (current.length >= 9) return

        _uiState.value = _uiState.value.copy(
            rawDigits = current + digit.toString()
        )
        recalculate()
    }

    fun onDeletePressed() {
        val current = _uiState.value.rawDigits
        val updated = if (current.isNotEmpty()) current.dropLast(1) else ""

        _uiState.value = _uiState.value.copy(rawDigits = updated)
        recalculate()
    }

    fun updateCustomPercent(value: Float) {
        _uiState.value = _uiState.value.copy(
            customTipPercent = value.roundToInt().toFloat()
        )
        recalculate()
    }

    private fun recalculate() {
        val state = _uiState.value

        val amount = digitsToAmount(state.rawDigits)
        val fixedPercent = 15.0
        val customPercent = state.customTipPercent.toDouble()

        val fixedTip = amount * (fixedPercent / 100.0)
        val customTip = amount * (customPercent / 100.0)

        val fixedTotal = amount + fixedTip
        val customTotal = amount + customTip

        _uiState.value = state.copy(
            formattedAmount = currencyFormatter.format(amount),
            formattedFixedTip = currencyFormatter.format(fixedTip),
            formattedCustomTip = currencyFormatter.format(customTip),
            formattedFixedTotal = currencyFormatter.format(fixedTotal),
            formattedCustomTotal = currencyFormatter.format(customTotal)
        )
    }

    private fun digitsToAmount(rawDigits: String): Double {
        if (rawDigits.isEmpty()) return 0.0
        return rawDigits.toLong().toDouble() / 100.0
    }
}
