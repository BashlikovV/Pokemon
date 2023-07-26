package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class PokemonListDto(
    @SerializedName("next") val next: String?,
    @SerializedName("previous") val previous: String?,
    @SerializedName("count") val count: Int?,
    @SerializedName("results") val results: List<PokemonDto>?
)