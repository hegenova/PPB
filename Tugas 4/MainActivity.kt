package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DiceRollerApp()
        }
    }
}

@Composable
fun DiceRollerApp() {
    var diceRoll by remember { mutableStateOf(1) }
    var isRolling by remember { mutableStateOf(false) }
    val alphaAnim = remember { Animatable(1f) }
    val rotationAnim = remember { Animatable(0f) }

    // New color scheme
    val backgroundColor = Color(0xFF2C3E50)  // Dark blue-gray
    val accentColor = Color(0xFFE74C3C)      // Coral red
    val buttonColor = Color(0xFF3498DB)      // Bright blue
    val textColor = Color(0xFFECF0F1)        // Off-white

    LaunchedEffect(isRolling) {
        if (isRolling) {
            // Rotation animation
            rotationAnim.animateTo(
                targetValue = 360f,
                animationSpec = tween(500, easing = LinearEasing)
            )
            rotationAnim.snapTo(0f)

            repeat(6) {
                diceRoll = Random.nextInt(1, 7)
                alphaAnim.animateTo(0.5f, animationSpec = tween(100))
                alphaAnim.animateTo(1f, animationSpec = tween(100))
                delay(100)
            }
            isRolling = false
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Custom dice icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accentColor, RoundedCornerShape(4.dp))
                        .border(1.dp, textColor, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚄",
                        fontSize = 20.sp,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "COSMIC DICE",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Custom dice icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accentColor, RoundedCornerShape(4.dp))
                        .border(1.dp, textColor, RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⚄",
                        fontSize = 20.sp,
                        color = textColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF34495E))
                    .clickable { if (!isRolling) isRolling = true },
                contentAlignment = Alignment.Center
            ) {
                // Custom dice drawing
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .alpha(alphaAnim.value)
                        .rotate(rotationAnim.value)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(2.dp, Color.Gray, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Draw dice dots based on current roll
                    DiceFace(number = diceRoll)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { if (!isRolling) isRolling = true },
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                modifier = Modifier
                    .height(56.dp)
                    .width(180.dp),
                shape = RoundedCornerShape(28.dp)
            ) {
                // Custom dice text
                Text(
                    text = "🎲",
                    fontSize = 20.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ROLL",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Current Roll: $diceRoll",
                fontSize = 22.sp,
                color = accentColor,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tap the dice or press the button",
                fontSize = 16.sp,
                color = textColor.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun DiceFace(number: Int) {
    Canvas(modifier = Modifier.size(120.dp)) {
        val dotRadius = size.width / 10
        val dotColor = Color.Black

        // Draw dots based on the dice number
        when (number) {
            1 -> {
                // Center dot
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = center
                )
            }
            2 -> {
                // Top-right and bottom-left dots
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.75f)
                )
            }
            3 -> {
                // Center dot
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = center
                )
                // Top-right and bottom-left dots
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.75f)
                )
            }
            4 -> {
                // Four corner dots
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.75f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.75f)
                )
            }
            5 -> {
                // Four corner dots
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.75f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.75f)
                )
                // Center dot
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = center
                )
            }
            6 -> {
                // Six dots (two columns of three)
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.5f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.25f, size.height * 0.75f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.25f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.5f)
                )
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(size.width * 0.75f, size.height * 0.75f)
                )
            }
        }
    }
}
