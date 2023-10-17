package by.bashlikovvv.pokemon.domain.usecase

import by.bashlikovvv.pokemon.data.DetailsNotFoundException
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.repository.IPokemonDetailsRepository
import kotlin.jvm.Throws

class GetPokemonDetailsByIdUseCase(private val pokemonDetailsRepository: IPokemonDetailsRepository) {

    /**
    * @throws DetailsNotFoundException
    * @param id the identifier of pokemon
    * @return [PokemonDetails]
    * */
    @Throws(DetailsNotFoundException::class)
    suspend fun getDetails(id: Int): PokemonDetails {
        return pokemonDetailsRepository.getDetails(id)
    }
}