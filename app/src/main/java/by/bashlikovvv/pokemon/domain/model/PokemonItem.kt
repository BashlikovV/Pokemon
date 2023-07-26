package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class PokemonItem(
    val name: String,
    val id: Long,
    val sprites: Map<String, Bitmap>
)
