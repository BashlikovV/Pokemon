package by.bashlikovvv.pokemon.domain.usecase

import androidx.paging.PagingData
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform

class GetPokemonByNameUseCase(private val pokemonListRepository: IPokemonListRepository) {

    fun execute(name: String): Flow<PagingData<PokemonItem>> {
        return pokemonListRepository.getList().transform { pagingData ->
            /*pagingData.map {
                val list = mutableListOf<PokemonItem>()

                if (it.name.contains(name, true)) {
                    list.add(it)
                }

                emit(PagingData.from(list))
            }*/
        }
    }
}