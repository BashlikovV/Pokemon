package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class TypeXDto(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)