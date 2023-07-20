package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap
import by.bashlikovvv.pokemon.domain.model.ability.PokemonAbility
import by.bashlikovvv.pokemon.domain.model.stat.PokemonStat
import by.bashlikovvv.pokemon.domain.model.type.PokemonType
import com.google.gson.annotations.SerializedName

data class PokemonDetails(
    @SerializedName("id") private val pokemonId: Long,
    @SerializedName("name") private val pokemonName: String,
    @SerializedName("sprites") private val pokemonSprites: List<Bitmap>,
    @SerializedName("types") private val pokemonTypes: List<PokemonType>,
    @SerializedName("stats") private val pokemonStats: List<PokemonStat>,
    @SerializedName("weight") private val pokemonWeight: Int,
    @SerializedName("order") private val pokemonOrder: Int,
    @SerializedName("abilities") private val pokemonAbilities: List<PokemonAbility>
)
