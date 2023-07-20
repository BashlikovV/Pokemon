package by.bashlikovvv.pokemon.domain.model.stat

import com.google.gson.annotations.SerializedName

sealed class Stats(
    @SerializedName("name") private val statName: String,
    @SerializedName("url") private val statUrl: String
) {
    class Hp(statName: String = "hp", statUrl: String) : Stats(statName, statUrl)

    class Attack(statName: String = "attack", statUrl: String) : Stats(statName, statUrl)

    class Defence(statName: String = "defense", statUrl: String) : Stats(statName, statUrl)

    class SpecialAttack(statName: String = "special-attack", statUrl: String) : Stats(statName, statUrl)

    class SpecialDefence(statName: String = "special-defense", statUrl: String) : Stats(statName, statUrl)

    class Speed(statName: String = "speed", statUrl: String) : Stats(statName, statUrl)
}

data class PokemonStat(
    @SerializedName("base_stat") private val baseState: Int,
    @SerializedName("effort") private val effort: Int,
    @SerializedName("stat") private val stat: Stats
)
