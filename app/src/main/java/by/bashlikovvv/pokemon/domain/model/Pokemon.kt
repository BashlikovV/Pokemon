package by.bashlikovvv.pokemon.domain.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") val pokemonName: String,
    @SerializedName("url") val pokemonUrl: String,
    @SerializedName("id") val pokemonId: Long
)
