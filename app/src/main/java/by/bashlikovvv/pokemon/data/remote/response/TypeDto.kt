package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class TypeDto(
    @SerializedName("slot") val slot: Int?,
    @SerializedName("type") val typeXDto: TypeXDto
)