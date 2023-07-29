package by.bashlikovvv.pokemon.data.repository

import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsDtoMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonDtoMapper
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import by.bashlikovvv.pokemon.utils.getBitmapWithGlide

class PokemonListRepository(
    private val cm: ConnectivityManager?,
    private val pokemonListApi: PokemonListApi,
    private val pokemonDetailsApi: PokemonDetailsApi
) : IPokemonListRepository {

    private val pokemonDtoMapper = PokemonDtoMapper()

    override suspend fun getList(): List<PokemonItem> {
        return  apiGetList()
    }

    private suspend fun apiGetList(): List<PokemonItem> {
        return if (isConnected()) {
            val pokemonListResponse = pokemonListApi.getPokemonList()
            val result = pokemonListResponse.body()!!.results.map {
                pokemonDtoMapper.mapFromEntity(it)
            }
            result.forEach {
                val body = pokemonDetailsApi.getPokemonDetailsById(it.id)
                val pokemonDetailsDto = body.body()!!
                val sprites = PokemonDetailsDtoMapper(
                    getSprites(pokemonDetailsDto.spritesDto)
                ).mapFromEntity(pokemonDetailsDto).sprites.sprites
                it.sprites.putAll(sprites)
            }

            return result
        } else {
            listOf()
        }
    }

    private fun isConnected(): Boolean {
        var isConnected = false
        cm?.run {
            cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                isConnected = when {
                    hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            }
        }
        return isConnected
    }

    private suspend fun getSprites(spritesDto: SpritesDto): Sprites {
        val sprites = mutableMapOf<String, Bitmap?>()
        sprites[SpriteNames.FrontShiny().name] = getBitmapWithGlide(spritesDto.frontShiny)

        return Sprites(sprites)
    }
}