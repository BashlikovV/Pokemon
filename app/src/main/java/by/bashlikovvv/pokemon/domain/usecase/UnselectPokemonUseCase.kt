package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.data.repository.PokemonStateRepository

class UnselectPokemonUseCase(private val pokemonStateRepository: PokemonStateRepository) {

    suspend fun execute() = pokemonStateRepository.unselectPokemon()
}