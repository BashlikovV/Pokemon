package by.bashlikovvv.pokemon.domain.repository

import androidx.paging.PagingData
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import kotlinx.coroutines.flow.Flow

interface IPokemonListRepository {

    fun getList(): Flow<PagingData<PokemonItem>>
}