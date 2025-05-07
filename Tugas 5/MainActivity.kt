package com.example.calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                CalculatorApp(applicationContext)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorApp(applicationContext: android.content.Context) {
    var num1 by remember { mutableStateOf("0") }
    var num2 by remember { mutableStateOf("0") }
    var resultText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Calculator",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Result display
            if (resultText.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = resultText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.End,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Input fields
            OutlinedTextField(
                value = num1,
                onValueChange = { num1 = it },
                label = { Text("First Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = num2,
                onValueChange = { num2 = it },
                label = { Text("Second Number") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Calculator buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    text = "Add",
                    onClick = {
                        try {
                            val result = num1.toInt() + num2.toInt()
                            resultText = "Result: $result"
                            Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary
                )

                CalculatorButton(
                    text = "Sub",
                    onClick = {
                        try {
                            val result = num1.toInt() - num2.toInt()
                            resultText = "Result: $result"
                            Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                CalculatorButton(
                    text = "Mul",
                    onClick = {
                        try {
                            val result = num1.toInt() * num2.toInt()
                            resultText = "Result: $result"
                            Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.tertiary
                )

                CalculatorButton(
                    text = "Div",
                    onClick = {
                        try {
                            if (num2.toInt() == 0) {
                                Toast.makeText(applicationContext, "Cannot divide by zero", Toast.LENGTH_SHORT).show()
                                return@CalculatorButton
                            }
                            val result = num1.toInt() / num2.toInt()
                            resultText = "Result: $result"
                            Toast.makeText(applicationContext, "Result is $result", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(applicationContext, "Invalid input", Toast.LENGTH_SHORT).show()
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(150.dp)
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CalculatorTheme(content: @Composable () -> Unit) {
    // Custom color scheme
    val colorScheme = darkColorScheme(
        primary = Color(0xFF4CAF50),       // Green
        onPrimary = Color.White,
        primaryContainer = Color(0xFF1E3A20),
        onPrimaryContainer = Color(0xFFB6F2B8),
        secondary = Color(0xFF2196F3),     // Blue
        onSecondary = Color.White,
        tertiary = Color(0xFFFF9800),      // Orange
        onTertiary = Color.Black,
        error = Color(0xFFE91E63),         // Pink
        background = Color(0xFF121212),    // Dark background
        surface = Color(0xFF1E1E1E),
        onBackground = Color.White,
        onSurface = Color.White
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(
            headlineMedium = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                letterSpacing = 0.5.sp
            ),
            bodyLarge = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                letterSpacing = 0.5.sp
            )
        ),
        content = content
    )
}
