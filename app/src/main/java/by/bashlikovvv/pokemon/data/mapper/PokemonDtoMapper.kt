package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.remote.response.PokemonDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.utils.Constants.Companion.BASE_URL

class PokemonDtoMapper : Mapper<PokemonDto, PokemonItem> {

    override fun mapFromEntity(entity: PokemonDto): PokemonItem {
        return PokemonItem(
            name = entity.name,
            id = entity.getId(),
            sprite = ""
        )
    }

    override fun mapToEntity(domain: PokemonItem): PokemonDto {
        throw UnsupportedOperationException("Not implemented")
    }

    private fun PokemonDto.getId(): Int = this.url
        .replace("${BASE_URL}pokemon/", "")
        .replace("/", "")
        .toInt()
}