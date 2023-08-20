package by.bashlikovvv.pokemon.data.repository

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.pokemon.data.DetailsNotFoundException
import by.bashlikovvv.pokemon.data.local.dao.PokemonDetailsDao
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsDtoMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsEntityMapper
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.repository.IPokemonDetailsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PokemonDetailsRepository @Inject constructor(
    private val cm: ConnectivityManager?,
    private val pokemonDetailsApi: PokemonDetailsApi,
    private val pokemonDetailsDao: PokemonDetailsDao
) : IPokemonDetailsRepository {

    private val pokemonDetailsEntityMapper = PokemonDetailsEntityMapper()

    @Throws(DetailsNotFoundException::class)
    override suspend fun getDetails(id: Int): PokemonDetails {
        return if (isConnected()) {
            getDetailsOnline(id)
        } else {
            getDetailsOffline(id)
        }
    }

    @Throws(DetailsNotFoundException::class)
    private suspend fun getDetailsOnline(id: Int): PokemonDetails {
        val pokemonDetailsResponse = pokemonDetailsApi.getPokemonDetailsById(id)
        val pokemonDetailsDto = pokemonDetailsResponse.body()!!
        val pokemonDetailsDtoMapper = PokemonDetailsDtoMapper(getSprites(pokemonDetailsDto.spritesDto))
        val result = pokemonDetailsDtoMapper.mapFromEntity(pokemonDetailsDto)

        withContext(Dispatchers.Default) {
            pokemonDetailsDao.insertDetails(pokemonDetailsEntityMapper.mapToEntity(result))
        }

        return result
    }

    @Throws(DetailsNotFoundException::class)
    private suspend fun getDetailsOffline(id: Int): PokemonDetails {
        return pokemonDetailsEntityMapper.mapFromEntity(
            pokemonDetailsDao.selectDetails(id) ?: throw DetailsNotFoundException()
        )
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

    private fun getSprites(spritesDto: SpritesDto): Sprites {
        val sprites = mutableMapOf<String, String?>()
        sprites[SpriteNames.BackDefault().name] = spritesDto.backDefault
        sprites[SpriteNames.BackFemale().name] = spritesDto.backFemale
        sprites[SpriteNames.BackShiny().name] = spritesDto.backShiny
        sprites[SpriteNames.BackShinyFemale().name] = spritesDto.backShinyFemale
        sprites[SpriteNames.FrontDefault().name] = spritesDto.frontDefault
        sprites[SpriteNames.FrontFemale().name] = spritesDto.frontFemale
        sprites[SpriteNames.FrontShiny().name] = spritesDto.frontShiny
        sprites[SpriteNames.FrontShinyFemale().name] = spritesDto.frontShinyFemale
        /*sprites[SpriteNames.Other.DreamWorld.FrontFemale().component1] = spritesDto.other.dreamWorld.frontFemale
        sprites[SpriteNames.Other.DreamWorld.FrontDefault().component1] = spritesDto.other.dreamWorld.frontDefault*/
        sprites[SpriteNames.Other.OfficialArtwork.FrontDefault().component1] = spritesDto.other.officialArtworkDto.frontDefault
        sprites[SpriteNames.Other.OfficialArtwork.FrontShiny().component1] = spritesDto.other.officialArtworkDto.frontShiny
        sprites[SpriteNames.Other.Home.FrontDefault().component1] = spritesDto.other.home.frontDefault
        sprites[SpriteNames.Other.Home.FrontFemale().component1] = spritesDto.other.home.frontFemale
        sprites[SpriteNames.Other.Home.FrontShinyFemale().component1] = spritesDto.other.home.frontShinyFemale
        sprites[SpriteNames.Other.Home.FrontShiny().component1] = spritesDto.other.home.frontShiny

        return Sprites(sprites)
    }
}