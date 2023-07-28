package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class PokemonItem(
    val name: String,
    val id: Int,
    val sprites: MutableMap<String, Bitmap?>
)
