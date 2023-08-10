package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.remote.response.PokemonPageDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem

class PokemonPageDtoMapper : Mapper<PokemonPageDto, List<PokemonItem>> {
    override fun mapFromEntity(entity: PokemonPageDto): List<PokemonItem> {
        val pokemonDtoMapper = PokemonDtoMapper()
        return entity.results.map { pokemonDtoMapper.mapFromEntity(it) }
    }

    override fun mapToEntity(domain: List<PokemonItem>): PokemonPageDto {
        throw UnsupportedOperationException("Not implemented")
    }
}