package by.bashlikovvv.pokemon.domain.repository

import by.bashlikovvv.pokemon.domain.model.PokemonItem

interface IPokemonListRepository {

    suspend fun getList(): List<PokemonItem>
}