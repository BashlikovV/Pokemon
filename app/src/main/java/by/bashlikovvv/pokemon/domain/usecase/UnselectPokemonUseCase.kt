package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.data.repository.PokemonStateRepository

class UnselectPokemonUseCase(private val pokemonStateRepository: PokemonStateRepository) {

    /**
     * Cancel the selection of all Pokemon in the database
     * */
    suspend fun execute() = pokemonStateRepository.unselectPokemon()
}