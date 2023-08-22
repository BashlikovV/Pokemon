package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.data.repository.PokemonStateRepository

class UpdatePokemonUseCase(private val pokemonStateRepository: PokemonStateRepository) {

    suspend fun execute(id: Int, selected: Boolean) {
        pokemonStateRepository.selectPokemon(id, selected)
    }
}