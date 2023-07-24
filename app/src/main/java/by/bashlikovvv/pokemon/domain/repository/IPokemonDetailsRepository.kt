package by.bashlikovvv.pokemon.domain.repository

import by.bashlikovvv.pokemon.domain.model.PokemonDetails

interface IPokemonDetailsRepository {

    suspend fun getDetails(id: Int): PokemonDetails
}