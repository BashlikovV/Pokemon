package by.bashlikovvv.pokemon.domain.usecase

import androidx.paging.PagingData
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import kotlinx.coroutines.flow.Flow

class GetPokemonListUseCase(private val pokemonListRepository: IPokemonListRepository) {

    fun execute(): Flow<PagingData<PokemonItem>> {
        return pokemonListRepository.getList()
    }
}