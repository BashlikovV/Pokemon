package by.bashlikovvv.pokemon.domain.model

data class PokemonItem(
    val name: String = "",
    val id: Int = 0,
    var sprite: String = "",
    var selected: Boolean = false
) {

    fun isEmpty(): Boolean {
        if (name.isNotEmpty()) return false
        if (sprite.isNotEmpty()) return false
        if (selected) return false

        return id != 0
    }
}