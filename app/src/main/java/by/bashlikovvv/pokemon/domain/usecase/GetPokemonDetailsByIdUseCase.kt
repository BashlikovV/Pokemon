package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.repository.IPokemonDetailsRepository

class GetPokemonDetailsByIdUseCase(private val pokemonDetailsRepository: IPokemonDetailsRepository) {

    suspend fun getDetails(id: Int): PokemonDetails {
        return pokemonDetailsRepository.getDetails(id)
    }
}