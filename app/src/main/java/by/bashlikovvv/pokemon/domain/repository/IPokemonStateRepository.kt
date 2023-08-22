package by.bashlikovvv.pokemon.domain.repository

interface IPokemonStateRepository {

    suspend fun selectPokemon(id: Int, selected: Boolean)

    suspend fun unselectPokemon()
}