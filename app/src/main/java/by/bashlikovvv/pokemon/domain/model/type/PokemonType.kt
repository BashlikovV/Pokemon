package by.bashlikovvv.pokemon.domain.model.type

import com.google.gson.annotations.SerializedName

data class PokemonType constructor(
    @SerializedName("slot") private val typeSlot: Int,
    @SerializedName("name") private val typeName: String,
    @SerializedName("url") private val typeUrl: String
)
