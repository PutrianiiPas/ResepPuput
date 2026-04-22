package com.example.etsresepapp

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.Alignment
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip

fun highlightText(text: String, query: String): AnnotatedString {
    if (query.isEmpty()) {
        return AnnotatedString(text)
    }

    val startIndex = text.lowercase().indexOf(query.lowercase())
    if (startIndex == -1) {
        return AnnotatedString(text)
    }

    val endIndex = startIndex + query.length

    return buildAnnotatedString {
        append(text.substring(0, startIndex))

        withStyle(
            style = SpanStyle(
                color = Color.Magenta,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(text.substring(startIndex, endIndex))
        }

        append(text.substring(endIndex))
    }
}

@Composable
fun MainScreen() {
    val recipeList = DummyData.listRecipe

    var selectedRecipe by remember { mutableStateOf<Recipe?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1000)
        isLoading = false
    }

    val filteredList = recipeList.filter { recipe ->
        recipe.name.contains(searchQuery, ignoreCase = true)/* ||
        recipe.ingredients.any { it.contains(searchQuery, ignoreCase = true) } ||
        recipe.seasonings.any { it.contains(searchQuery, ignoreCase = true) } ||*/
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (selectedRecipe == null) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    ))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoresep),
                    contentDescription = "Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.5f))

                )

                Text(
                    text = "Resep  Puput",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Cari resep...") }
            )

            if (filteredList.isEmpty()) {
                Text(
                    text = "Tidak ada resep yang ditemukan",
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredList) { recipe ->
                        RecipeItem(recipe, searchQuery) {
                            selectedRecipe = recipe
                        }
                    }
                }
            }
        }
    } else {
            DetailScreen(
                recipe = selectedRecipe!!,
                onBack = { selectedRecipe = null }
            )
    }
}

@Composable
fun RecipeItem(recipe: Recipe, query: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = ""
    )

    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(recipe.image),
                contentDescription = recipe.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = highlightText(recipe.name, query),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}