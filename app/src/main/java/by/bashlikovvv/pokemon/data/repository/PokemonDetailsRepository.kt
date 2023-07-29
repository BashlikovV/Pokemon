package by.bashlikovvv.pokemon.data.repository

import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.pokemon.data.NetworkException
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsDtoMapper
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.repository.IPokemonDetailsRepository
import by.bashlikovvv.pokemon.utils.getBitmapWithGlide

class PokemonDetailsRepository(
    private val cm: ConnectivityManager?,
    private val pokemonDetailsApi: PokemonDetailsApi
) : IPokemonDetailsRepository {

    override suspend fun getDetails(id: Int): PokemonDetails {
        return apiGetDetails(id)
    }

    private suspend fun apiGetDetails(id: Int): PokemonDetails {
        return if (isConnected()) {
            val pokemonDetailsResponse = pokemonDetailsApi.getPokemonDetailsById(id)
            val pokemonDetailsDto = pokemonDetailsResponse.body()!!
            val pokemonDetailsDtoMapper = PokemonDetailsDtoMapper(getSprites(pokemonDetailsDto.spritesDto))
            pokemonDetailsDtoMapper.mapFromEntity(pokemonDetailsDto)
        } else {
            throw NetworkException()
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
        sprites[SpriteNames.BackDefault().name] = getBitmapWithGlide(spritesDto.backDefault)
        sprites[SpriteNames.BackFemale().name] = getBitmapWithGlide(spritesDto.backFemale)
        sprites[SpriteNames.BackShiny().name] = getBitmapWithGlide(spritesDto.backShiny)
        sprites[SpriteNames.BackShinyFemale().name] = getBitmapWithGlide(spritesDto.backShinyFemale)
        sprites[SpriteNames.FrontDefault().name] = getBitmapWithGlide(spritesDto.frontDefault)
        sprites[SpriteNames.FrontFemale().name] = getBitmapWithGlide(spritesDto.frontFemale)
        sprites[SpriteNames.FrontShiny().name] = getBitmapWithGlide(spritesDto.frontShiny)
        sprites[SpriteNames.FrontShinyFemale().name] = getBitmapWithGlide(spritesDto.frontShinyFemale)

        return Sprites(sprites)
    }
}