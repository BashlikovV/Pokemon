package by.bashlikovvv.pokemon.data.mapper

import by.bashlikovvv.pokemon.data.remote.response.PokemonDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.utils.Constants.Companion.BASE_URL

class PokemonDtoMapper(private val sprites: Sprites): Mapper<PokemonDto, PokemonItem> {
    override fun mapFromEntity(entity: PokemonDto): PokemonItem {
        val id = entity.url
            .replace("${BASE_URL}pokemon/", "")
            .replace("/", "")
            .toInt()
        return PokemonItem(
            name = entity.name,
            id = id.toLong(),
            sprites = sprites.sprites
        )
    }

    override fun mapToEntity(domain: PokemonItem): PokemonDto {
        throw UnsupportedOperationException("Not implemented")
    }
}