package by.bashlikovvv.pokemon.domain.model.ability

import com.google.gson.annotations.SerializedName

data class PokemonAbility(
    @SerializedName("ability") private val ability: Ability,
    @SerializedName("is_hidden") private val isHidden: Boolean,
    @SerializedName("slot") private val slot: Int
)
