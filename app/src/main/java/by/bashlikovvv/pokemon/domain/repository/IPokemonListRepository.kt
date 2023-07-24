package by.bashlikovvv.pokemon.domain.repository

import by.bashlikovvv.pokemon.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface IPokemonListRepository {

    fun getList(): Flow<List<Pokemon>>
}