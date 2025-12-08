package org.example.project

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.random.Random

class Greeting {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend fun fetchPokemon(pokemonId: Int? = null): Pokemon {
        val selectedId = pokemonId ?: Random.nextInt(POKEDEX_MIN_ID, POKEDEX_MAX_ID + 1)
        return client.get("$BASE_URL$selectedId").body()
    }

    companion object {
        private const val BASE_URL = "https://tyradex.vercel.app/api/v1/pokemon/"
        private const val POKEDEX_MIN_ID = 1
        private const val POKEDEX_MAX_ID = 1025
    }
}
