package by.bashlikovvv.pokemon.domain.model.ability

import com.google.gson.annotations.SerializedName

data class Ability(
    @SerializedName("name") private val abilityName: String,
    @SerializedName("url") private val abilityUrl: String
)
