package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class PokemonDetails(
    val id: Long,
    val name: String,
    val sprites: List<Bitmap>,
    val types: List<String>,
    val weightInHg: Int,
    val heightInDm: Int
)
