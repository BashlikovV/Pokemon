package by.bashlikovvv.pokemon.data.mapper

import android.content.Context
import by.bashlikovvv.pokemon.data.remote.response.PokemonListDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.Sprites

class PokemonListDtoMapper(private val context: Context) : Mapper<PokemonListDto, List<PokemonItem>> {
    override fun mapFromEntity(entity: PokemonListDto): List<PokemonItem> {
        return entity.results!!.map {

            val pokemonItem = PokemonDtoMapper(getPokemonSprites(it.url)).mapFromEntity(it)
            PokemonItem(
                name = pokemonItem.name,
                id = pokemonItem.id,
                sprites = pokemonItem.sprites
            )
        }
    }

    override fun mapToEntity(domain: List<PokemonItem>): PokemonListDto {
        throw UnsupportedOperationException("Not implemented")
    }

    private fun getPokemonSprites(url: String): Sprites {
        TODO()
        return Sprites(mapOf())
    }
}