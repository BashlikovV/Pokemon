package by.bashlikovvv.pokemon.data.remote.response

import com.google.gson.annotations.SerializedName

data class PokemonDetailsDto(
    @SerializedName("location_area_encounters") val locationAreaEncounters: String,
    @SerializedName("types") val types: List<TypeDto>,
    @SerializedName("base_experience") val baseExperience: Int?,
    @SerializedName("weight") val weight: Int,
    @SerializedName("is_default") val isDefault: Boolean,
    @SerializedName("sprites") val spritesDto: SpritesDto,
    @SerializedName("abilities") val abilities: List<AbilitiesXDto>,
    @SerializedName("species") val speciesDto: SpeciesDto,
    @SerializedName("stats") val stats: List<StatsDto>,
    @SerializedName("name") val name: String,
    @SerializedName("id") val id: Int,
    @SerializedName("forms") val forms: List<FormsDto>,
    @SerializedName("height") val height: Int,
    @SerializedName("order") val order: Int
)