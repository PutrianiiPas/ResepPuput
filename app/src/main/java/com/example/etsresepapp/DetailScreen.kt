package com.example.etsresepapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun DetailScreen(recipe: Recipe, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = recipe.image),
                contentDescription = recipe.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.titleLarge,
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(text = recipe.description)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    "Bahan:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                recipe.ingredients.forEach {
                    Text("- $it")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Bumbu:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                recipe.seasonings.forEach {
                    Text("- $it")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Langkah-langkah:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                recipe.steps.forEachIndexed { index, step ->
                    Text("${index + 1}. $step")
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }

        Button(
            onClick = { onBack() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) { Text("Kembali", fontWeight = FontWeight.Bold) }
    }
}