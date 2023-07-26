package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class AbilitiesXDto(
    @SerializedName("is_hidden") val isHidden: Boolean?,
    @SerializedName("ability") val abilityDto: AbilityDto,
    @SerializedName("slot") val slot: Int?
)