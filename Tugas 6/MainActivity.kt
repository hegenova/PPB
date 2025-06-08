package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.FlowPreview
import com.example.currencyconverter.ui.theme.CurrencyConverterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CurrencyConverter()
                }
            }
        }
    }
}

// Enhanced exchange rates data
private val exchangeRates = mapOf(
    "IDR" to 1.0,
    "USD" to 16823.0,
    "EUR" to 19150.0,
    "JPY" to 117.06,
    "SGD" to 12570.0,
    "AUD" to 11350.0,
    "GBP" to 22450.0,
    "CHF" to 19750.0,
    "CAD" to 12400.0,
    "CNY" to 2418.0
)

// Currency display names
private val currencyNames = mapOf(
    "IDR" to "Indonesian Rupiah",
    "USD" to "US Dollar",
    "EUR" to "Euro",
    "JPY" to "Japanese Yen",
    "SGD" to "Singapore Dollar",
    "AUD" to "Australian Dollar",
    "GBP" to "British Pound",
    "CHF" to "Swiss Franc",
    "CAD" to "Canadian Dollar",
    "CNY" to "Chinese Yuan"
)

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun CurrencyConverter() {
    val currencies = remember { listOf("IDR", "USD", "EUR", "JPY", "SGD", "AUD", "GBP", "CHF", "CAD", "CNY") }
    var amountInput by remember { mutableStateOf("") }
    var fromCurrency by remember { mutableStateOf("IDR") }
    var toCurrency by remember { mutableStateOf("USD") }

    // Modern color scheme
    val gradientColors = listOf(
        Color(0xFF667eea),
        Color(0xFF764ba2)
    )
    val accentColor = Color(0xFF4CAF50)
    val surfaceColor = MaterialTheme.colorScheme.surface

    // Debounced amount calculation
    val debouncedAmountFlow = remember { MutableStateFlow("") }
    val debouncedAmount by produceState("") {
        debouncedAmountFlow.debounce(300).collect { value = it }
    }

    LaunchedEffect(amountInput) {
        debouncedAmountFlow.value = amountInput
    }

    val convertedAmount by remember(debouncedAmount, fromCurrency, toCurrency) {
        derivedStateOf {
            val amount = debouncedAmount.toDoubleOrNull() ?: 0.0
            convertCurrency(amount, fromCurrency, toCurrency)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header with modern styling
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = surfaceColor.copy(alpha = 0.9f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(gradientColors),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "💰",
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            "Currency Exchange",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp
                            ),
                            color = Color.White
                        )

                        Text(
                            "Convert currencies worldwide",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            // Amount input with modern design
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Amount",
                            tint = accentColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Enter Amount",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = accentColor
                        )
                    }

                    OutlinedTextField(
                        value = amountInput,
                        onValueChange = {
                            if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                amountInput = it
                            }
                        },
                        label = { Text("Amount to convert") },
                        placeholder = { Text("0.00") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // Currency selection with enhanced design
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = surfaceColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        "Select Currencies",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = accentColor
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // From Currency
                        ModernCurrencySelector(
                            label = "From",
                            selectedCurrency = fromCurrency,
                            onCurrencySelected = { fromCurrency = it },
                            currencies = currencies,
                            modifier = Modifier.weight(1f),
                            accentColor = accentColor
                        )

                        // Swap Button with animation effect
                        Button(
                            onClick = {
                                val temp = fromCurrency
                                fromCurrency = toCurrency
                                toCurrency = temp
                            },
                            modifier = Modifier.size(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = accentColor,
                                contentColor = Color.White
                            ),
                            shape = CircleShape,
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Swap currencies",
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // To Currency
                        ModernCurrencySelector(
                            label = "To",
                            selectedCurrency = toCurrency,
                            onCurrencySelected = { toCurrency = it },
                            currencies = currencies,
                            modifier = Modifier.weight(1f),
                            accentColor = accentColor
                        )
                    }
                }
            }

            // Result display with enhanced styling
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = accentColor.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Converted Amount",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = formatAmount(convertedAmount),
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            ),
                            color = accentColor,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = getCurrencySymbol(toCurrency),
                                style = MaterialTheme.typography.titleLarge,
                                color = accentColor
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = toCurrency,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = accentColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Exchange rate footer with modern styling
            if (fromCurrency != toCurrency) {
                val rate = getExchangeRate(fromCurrency, toCurrency)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "1 $fromCurrency = ${formatRate(rate)} $toCurrency",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun ModernCurrencySelector(
    label: String,
    selectedCurrency: String,
    onCurrencySelected: (String) -> Unit,
    currencies: List<String>,
    modifier: Modifier = Modifier,
    accentColor: Color
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor.copy(alpha = 0.1f),
                    contentColor = accentColor
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = getCurrencySymbol(selectedCurrency),
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = selectedCurrency,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Select currency"
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                currencies.forEach { currency ->
                    DropdownMenuItem(
                        text = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = getCurrencySymbol(currency),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.width(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        currency,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        currencyNames[currency] ?: currency,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        onClick = {
                            onCurrencySelected(currency)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

fun getCurrencySymbol(currency: String): String {
    return when (currency) {
        "USD" -> "$"
        "EUR" -> "€"
        "GBP" -> "£"
        "JPY" -> "¥"
        "CNY" -> "¥"
        "IDR" -> "Rp"
        "SGD" -> "S$"
        "AUD" -> "A$"
        "CAD" -> "C$"
        "CHF" -> "Fr"
        else -> "¤"
    }
}

fun getExchangeRate(from: String, to: String): Double {
    val rateFrom = exchangeRates[from] ?: 1.0
    val rateTo = exchangeRates[to] ?: 1.0
    return rateFrom / rateTo
}

fun convertCurrency(amount: Double, from: String, to: String): Double {
    if (from == to) return amount
    val rateFrom = exchangeRates[from] ?: 1.0
    val rateTo = exchangeRates[to] ?: 1.0
    val amountInIDR = amount * rateFrom
    return amountInIDR / rateTo
}

fun formatAmount(amount: Double): String {
    return when {
        amount >= 1_000_000 -> String.format("%.2fM", amount / 1_000_000)
        amount >= 1_000 -> String.format("%.2fK", amount / 1_000)
        else -> String.format("%.2f", amount)
    }
}

fun formatRate(rate: Double): String {
    return when {
        rate >= 1000 -> String.format("%.2f", rate)
        rate >= 1 -> String.format("%.4f", rate)
        else -> String.format("%.6f", rate)
    }
}
