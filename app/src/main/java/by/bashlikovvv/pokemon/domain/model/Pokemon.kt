package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class Pokemon(
    val name: String,
    val id: Long,
    val image: Bitmap,
    val sprite: Bitmap
)
