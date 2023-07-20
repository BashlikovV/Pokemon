package by.bashlikovvv.pokemon.domain.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("name") private val pokemonName: String,
    @SerializedName("url") private val pokemonUrl: String,
    @SerializedName("id") private val pokemonId: Long
)
