package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.data.repository.PokemonStateRepository

class UpdatePokemonUseCase(private val pokemonStateRepository: PokemonStateRepository) {

    /**
     * @param id the identifier of pokemon
     * @param selected indicates is selected Pokemon or not
     * */
    suspend fun execute(id: Int, selected: Boolean) {
        pokemonStateRepository.selectPokemon(id, selected)
    }
}