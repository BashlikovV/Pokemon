package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository

class GetPokemonByListUseCase(private val pokemonListRepository: IPokemonListRepository) {

    suspend fun execute(): List<PokemonItem> {
        return pokemonListRepository.getList()
    }
}