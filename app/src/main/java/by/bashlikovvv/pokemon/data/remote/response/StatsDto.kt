package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class StatsDto(
    @SerializedName("stat") val statXDto: StatXDto,
    @SerializedName("base_stat") val baseStat: Int?,
    @SerializedName("effort") val effort: Int?
)