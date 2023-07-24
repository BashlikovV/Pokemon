package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.domain.model.Pokemon
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import kotlinx.coroutines.flow.Flow

class GetPokemonByListUseCase(private val pokemonListRepository: IPokemonListRepository) {

    fun execute(): Flow<List<Pokemon>> {
        return pokemonListRepository.getList()
    }
}