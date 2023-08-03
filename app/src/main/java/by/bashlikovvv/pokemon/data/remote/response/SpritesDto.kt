package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class SpritesDto(
    @SerializedName("back_shiny_female") val backShinyFemale: String?,
    @SerializedName("back_female") val backFemale: String?,
    @SerializedName("back_default") val backDefault: String?,
    @SerializedName("front_shiny_female") val frontShinyFemale: String?,
    @SerializedName("front_default") val frontDefault: String?,
    @SerializedName("front_female") val frontFemale: String?,
    @SerializedName("back_shiny") val backShiny: String?,
    @SerializedName("front_shiny") val frontShiny: String?,
    @SerializedName("other") val other: OtherDto
)