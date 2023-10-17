package by.bashlikovvv.pokemon.domain.usecase

import androidx.paging.PagingData
import androidx.paging.filter
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class GetPokemonByNameUseCase(private val pokemonListRepository: IPokemonListRepository) {

    /**
     * @param name the name of the desired pokemon
     * @return Flow of PagingData with [PokemonItem]
     * */
    fun execute(name: String): Flow<PagingData<PokemonItem>> {
        return pokemonListRepository.getList().transform { pagingData ->
            emit(pagingData.filter { it.name.contains(name, true) })
        }
    }
}