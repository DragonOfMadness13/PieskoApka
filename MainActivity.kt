package com.example.pieskoapka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip
import com.example.pieskoapka.ui.theme.PieskoApkaTheme

data class Dog(val name: String, var isFavorite: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DogListApp()
        }
    }
}

@Composable
fun DogListApp() {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val dogs = remember { mutableStateListOf<Dog>() }
    var errorMessage by remember { mutableStateOf("") }
    var searchResult by remember { mutableStateOf<List<Dog>>(emptyList()) }

    if (dogs.isEmpty()) {
        repeat(1){ dogs.add(Dog("Donald"))}
        repeat(14) { dogs.add(Dog("Pan Punpernikiel")) }
    }

    val totalDogs = dogs.size
    val favoriteDogs = dogs.count { it.isFavorite }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Poszukaj lub dodaj pieska 🐕") },
                modifier = Modifier
                    .weight(1f)
                    .background(if (errorMessage.isNotEmpty()) Color.Red.copy(alpha = 0.1f) else Color.Transparent)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {
                searchResult = dogs.filter { it.name.contains(searchQuery.text.trim(), ignoreCase = true) }
            }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Search,
                    contentDescription = "Search Dog"
                )
            }
            IconButton(
                onClick = {
                    if (dogs.any { it.name.equals(searchQuery.text.trim(), ignoreCase = true) }) {
                        errorMessage = "Piesek jest już na liście"
                    } else {
                        dogs.add(Dog(searchQuery.text.trim()))
                        searchQuery = TextFieldValue("")
                        errorMessage = ""
                    }
                }) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.Add,
                    contentDescription = "Add Dog",
                    tint = Color.Black
                )
            }
        }
        if (errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        Text(
            text = "🐕: $totalDogs    💜: $favoriteDogs",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(searchResult.ifEmpty { dogs }) { dog ->
                DogListItem(dog, onFavoriteClick = {
                    dog.isFavorite = !dog.isFavorite
                    if (dog.isFavorite) {
                        dogs.remove(dog)
                        dogs.add(0, dog)
                    }
                }, onDeleteClick = {
                    dogs.remove(dog)
                })
            }
        }
    }
}

@Composable
fun DogListItem(dog: Dog, onFavoriteClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFFF5F5F5))
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFB39DDB)),
                contentAlignment = Alignment.Center
            ) {
                Text("🐕", fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(dog.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Row {
            IconButton(onClick = onFavoriteClick) {
                Text(
                    text = if (dog.isFavorite) "💜" else "🤍",
                    fontSize = 20.sp
                )
            }
            IconButton(onClick = onDeleteClick) {
                Icon(
                    tint = Color.Red,
                    imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                    contentDescription = "Delete"
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewDogListApp() {
    DogListApp()
}