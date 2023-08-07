package by.bashlikovvv.pokemon.domain.model

import android.graphics.Bitmap

data class PokemonDetails(
    val id: Int = 0,
    val name: String = "",
    val sprites: Sprites = Sprites(emptyMap()),
    val types: List<String> = emptyList(),
    val weightInHg: Int = 0,
    val heightInDm: Int = 0
)

data class Sprites(val sprites: Map<String, Bitmap?>)

sealed class SpriteNames(val name: String) {

    class BackShiny : SpriteNames("backShiny")

    class BackShinyFemale : SpriteNames("backShinyFemale")

    class BackFemale : SpriteNames("backFemale")

    class BackDefault : SpriteNames("backDefault")

    class FrontShinyFemale : SpriteNames("frontShinyFemale")

    class FrontDefault : SpriteNames("frontDefault")

    class FrontFemale : SpriteNames("frontFemale")

    class FrontShiny : SpriteNames("frontShiny")

    sealed class Other(val component0: String) : SpriteNames("other") {

        sealed class DreamWorld(val component1: String) : Other("dreamWorld") {

            class FrontDefault : DreamWorld("frontDefault")

            class FrontFemale : DreamWorld("frontFemale")
        }

        sealed class Home(val component1: String) : Other("home") {

            class FrontDefault : Home("frontDefault")

            class FrontFemale : Home("frontFemale")

            class FrontShiny : DreamWorld("frontShiny")

            class FrontShinyFemale : DreamWorld("frontShinyFemale")
        }

        sealed class OfficialArtwork(val component1: String) : Other("officialArtwork") {

            class FrontDefault : OfficialArtwork("frontDefault")

            class FrontShiny : OfficialArtwork("frontShiny")

        }
    }
}