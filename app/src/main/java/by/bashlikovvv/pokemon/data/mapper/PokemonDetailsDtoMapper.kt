package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.remote.response.PokemonDetailsDto
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.Sprites

class PokemonDetailsDtoMapper(
    private val sprites: Sprites
) : Mapper<PokemonDetailsDto, PokemonDetails> {
    override fun mapFromEntity(entity: PokemonDetailsDto): PokemonDetails {

        return PokemonDetails(
            id = entity.id.toLong(),
            name = entity.name,
            sprites = sprites,
            types = TypeDtoMapper().mapFromEntity(entity.types),
            weightInHg = entity.weight,
            heightInDm = entity.height
        )
    }

    override fun mapToEntity(domain: PokemonDetails): PokemonDetailsDto {
        throw UnsupportedOperationException("Not implemented")
    }
}