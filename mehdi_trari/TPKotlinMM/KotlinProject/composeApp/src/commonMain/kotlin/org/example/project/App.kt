package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun App() {
    MaterialTheme {
        PokemonQuizScreen()
    }
}

@Composable
private fun PokemonQuizScreen() {
    val greeting = remember { Greeting() }
    var pokemon by remember { mutableStateOf<Pokemon?>(null) }
    var guess by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf("Chargement du Pokemon...") }
    var score by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(true) }
    var refreshKey by remember { mutableStateOf(0) }

    suspend fun loadPokemon() {
        isLoading = true
        guess = ""
        feedback = "Chargement du Pokemon..."
        pokemon = runCatching { greeting.fetchPokemon() }.getOrElse {
            feedback = "Impossible de charger un Pokemon"
            null
        }
        isLoading = false
        if (pokemon != null) {
            feedback = "Quel est ce Pokemon ?"
        }
    }

    LaunchedEffect(refreshKey) {
        loadPokemon()
    }

    Column(
        modifier = Modifier
            .safeContentPadding()
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Pokemon affiche : ${pokemon?.name?.fr ?: "..."}",
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = guess,
            onValueChange = { guess = it },
            label = { Text("Nom du Pokemon") },
            singleLine = true,
            enabled = !isLoading && pokemon != null,
            modifier = Modifier.fillMaxWidth(),
        )
        Button(
            onClick = {
                val expected = pokemon?.name?.fr?.trim().orEmpty()
                if (expected.isEmpty()) {
                    feedback = "Aucun Pokemon a valider."
                    return@Button
                }
                if (guess.trim().equals(expected, ignoreCase = true)) {
                    score += 1
                    feedback = "Bravo, c'est $expected !"
                } else {
                    feedback = "Rate, c'etait $expected."
                }
            },
            enabled = guess.isNotBlank() && !isLoading && pokemon != null,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Valider")
        }
        Text(
            text = "Resultat : $feedback",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = "Score: $score",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Button(
            onClick = { refreshKey += 1 },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Nouveau Pokemon")
        }
    }
}
