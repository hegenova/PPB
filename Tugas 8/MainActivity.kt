package com.example.woof

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.woof.ui.theme.WoofTheme

// Simple data class for dogs
data class Dog(
    val name: String,
    val age: Int,
    val breed: String
)

// Sample dog data
val sampleDogs = listOf(
    Dog("Max", 3, "Golden Retriever"),
    Dog("Bella", 5, "Labrador"),
    Dog("Charlie", 2, "German Shepherd"),
    Dog("Luna", 4, "Border Collie"),
    Dog("Cooper", 6, "Poodle"),
    Dog("Daisy", 1, "Beagle"),
    Dog("Rocky", 7, "Bulldog"),
    Dog("Molly", 3, "Husky")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WoofTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WoofApp()
                }
            }
        }
    }
}

/**
 * Composable that displays an app bar and a list of dogs.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofApp() {
    Scaffold(
        topBar = {
            WoofTopAppBar()
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(sampleDogs) { dog ->
                DogItem(
                    dog = dog,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

/**
 * Composable that displays a list item containing a dog icon and their information.
 */
@Composable
fun DogItem(
    dog: Dog,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DogIcon()
            DogInformation(
                dogName = dog.name,
                dogAge = dog.age,
                dogBreed = dog.breed,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )
            // Add a heart icon for favorited dogs
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Composable that displays a Top Bar with an icon and text.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WoofTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Use a Home icon with gradient background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Home",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Text(
                    text = "Woof",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
    )
}

/**
 * Composable that displays a dog icon.
 */
@Composable
fun DogIcon(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.tertiaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Dog",
            tint = MaterialTheme.colorScheme.onTertiaryContainer,
            modifier = Modifier.size(40.dp)
        )
    }
}

/**
 * Composable that displays a dog's information.
 */
@Composable
fun DogInformation(
    dogName: String,
    dogAge: Int,
    dogBreed: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = dogName,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Text(
            text = "$dogAge years old",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 2.dp)
        )
        Text(
            text = dogBreed,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}

/**
 * Composable that displays what the UI of the app looks like in light theme.
 */
@Preview(showBackground = true)
@Composable
fun WoofPreview() {
    WoofTheme(darkTheme = false) {
        WoofApp()
    }
}

/**
 * Composable that displays what the UI of the app looks like in dark theme.
 */
@Preview(showBackground = true)
@Composable
fun WoofDarkThemePreview() {
    WoofTheme(darkTheme = true) {
        WoofApp()
    }
}
