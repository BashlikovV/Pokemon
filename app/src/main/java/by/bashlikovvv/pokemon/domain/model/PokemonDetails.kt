package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class PokemonDetails(
    val id: Long,
    val name: String,
    val sprites: Sprites,
    val types: List<String>,
    val weightInHg: Int,
    val heightInDm: Int
)

data class Sprites(val sprites: Map<String, Bitmap>)

sealed class SpriteNames(val name: String) {

    class BackShiny : SpriteNames("backShiny")

    class BackShinyFemale : SpriteNames("backShinyFemale")

    class BackFemale : SpriteNames("backFemale")

    class BackDefault : SpriteNames("backDefault")

    class FrontShinyFemale : SpriteNames("frontShinyFemale")

    class FrontDefault : SpriteNames("frontDefault")

    class FrontFemale : SpriteNames("frontFemale")

    class FrontShiny : SpriteNames("frontShiny")


}