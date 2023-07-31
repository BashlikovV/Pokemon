package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class DreamWorldDto(
    @SerializedName("front_default") val frontDefault: String,
    @SerializedName("front_female") val frontFemale: String
)
