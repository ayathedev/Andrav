package com.example.ui.apps

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonMagenta
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CalculatorApp(
    modifier: Modifier = Modifier
) {
    var displayValue by remember { mutableStateOf("0") }
    var expression by remember { mutableStateOf("") }
    var lastOperator by remember { mutableStateOf("") }
    var firstOperand by remember { mutableStateOf<Double?>(null) }
    var isNewInput by remember { mutableStateOf(true) }

    fun onDigit(digit: String) {
        if (isNewInput || displayValue == "0") {
            displayValue = digit
            isNewInput = false
        } else {
            if (displayValue.length < 12) {
                displayValue += digit
            }
        }
    }

    fun onOperator(op: String) {
        firstOperand = displayValue.toDoubleOrNull()
        lastOperator = op
        expression = "$displayValue $op"
        isNewInput = true
    }

    fun onEqual() {
        val second = displayValue.toDoubleOrNull()
        if (firstOperand != null && second != null && lastOperator.isNotEmpty()) {
            val res = when (lastOperator) {
                "+" -> firstOperand!! + second
                "-" -> firstOperand!! - second
                "×" -> firstOperand!! * second
                "÷" -> if (second != 0.0) firstOperand!! / second else Double.NaN
                else -> second
            }
            expression = "$firstOperand $lastOperator $second ="
            displayValue = if (res.isNaN()) "Error" else if (res % 1.0 == 0.0) res.toLong().toString() else res.toString()
            firstOperand = null
            lastOperator = ""
            isNewInput = true
        }
    }

    fun onClear() {
        displayValue = "0"
        expression = ""
        lastOperator = ""
        firstOperand = null
        isNewInput = true
    }

    fun onToggleSign() {
        if (displayValue != "0" && displayValue != "Error") {
            displayValue = if (displayValue.startsWith("-")) displayValue.substring(1) else "-$displayValue"
        }
    }

    fun onPercent() {
        val num = displayValue.toDoubleOrNull()
        if (num != null) {
            displayValue = (num / 100.0).toString()
        }
    }

    val buttons = listOf(
        listOf("C", "+/-", "%", "÷"),
        listOf("7", "8", "9", "×"),
        listOf("4", "5", "6", "-"),
        listOf("1", "2", "3", "+"),
        listOf("0", ".", "=")
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF161226))
            .padding(14.dp)
    ) {
        // Calculator Screen Display Box
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF221D3B))
                .border(1.dp, ChromeGlassBorder, RoundedCornerShape(16.dp))
                .padding(14.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = expression,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = displayValue,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1,
                modifier = Modifier.testTag("calculator_display")
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grid of Calculator Keypad Buttons
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            buttons.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    row.forEach { label ->
                        val isOp = label in listOf("÷", "×", "-", "+", "=")
                        val isSpecial = label in listOf("C", "+/-", "%")

                        Box(
                            modifier = Modifier
                                .weight(if (label == "0") 2f else 1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    when {
                                        label == "=" -> NeonCyan
                                        isOp -> Color(0xFF332B50)
                                        isSpecial -> Color(0xFF2B2244)
                                        else -> Color(0xFF201B35)
                                    }
                                )
                                .border(
                                    width = 1.dp,
                                    color = if (label == "=") NeonCyan else ChromeGlassBorder,
                                    shape = RoundedCornerShape(14.dp)
                                )
                                .clickable {
                                    when (label) {
                                        "C" -> onClear()
                                        "+/-" -> onToggleSign()
                                        "%" -> onPercent()
                                        "÷", "×", "-", "+" -> onOperator(label)
                                        "=" -> onEqual()
                                        else -> onDigit(label)
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = when {
                                        label == "=" -> Color(0xFF0F0C1B)
                                        isOp -> NeonCyan
                                        isSpecial -> NeonMagenta
                                        else -> TextPrimary
                                    },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
