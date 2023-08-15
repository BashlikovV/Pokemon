package by.bashlikovvv.pokemon.data.repository

import android.content.Context
import android.graphics.Bitmap
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
import com.bumptech.glide.Glide
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException
import javax.inject.Inject

class PokemonDetailsRepository @Inject constructor(
    private val cm: ConnectivityManager?,
    private val pokemonDetailsApi: PokemonDetailsApi,
    private val pokemonDetailsDao: PokemonDetailsDao,
    @ApplicationContext private val context: Context
) : IPokemonDetailsRepository {

    private val pokemonDetailsEntityMapper = PokemonDetailsEntityMapper(context)

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
        sprites[SpriteNames.Other.DreamWorld.FrontFemale().component1] = getBitmapWithGlide(spritesDto.other.dreamWorld.frontFemale)
        sprites[SpriteNames.Other.DreamWorld.FrontDefault().component1] = getBitmapWithGlide(spritesDto.other.dreamWorld.frontDefault)
        sprites[SpriteNames.Other.OfficialArtwork.FrontDefault().component1] = getBitmapWithGlide(spritesDto.other.officialArtworkDto.frontDefault)
        sprites[SpriteNames.Other.OfficialArtwork.FrontShiny().component1] = getBitmapWithGlide(spritesDto.other.officialArtworkDto.frontShiny)
        sprites[SpriteNames.Other.Home.FrontDefault().component1] = getBitmapWithGlide(spritesDto.other.home.frontDefault)
        sprites[SpriteNames.Other.Home.FrontFemale().component1] = getBitmapWithGlide(spritesDto.other.home.frontFemale)
        sprites[SpriteNames.Other.Home.FrontShinyFemale().component1] = getBitmapWithGlide(spritesDto.other.home.frontShinyFemale)
        sprites[SpriteNames.Other.Home.FrontShiny().component1] = getBitmapWithGlide(spritesDto.other.home.frontShiny)

        return Sprites(sprites)
    }

    private suspend fun getBitmapWithGlide(url: String?) = withContext(Dispatchers.IO) {
        if (url.isNullOrEmpty()) { return@withContext null }
        return@withContext try {
            val result = Glide.with(context)
                .asBitmap()
                .load(url)
                .centerCrop()
                .submit()
                .get()

            result
        } catch (e: ExecutionException) {
            e.printStackTrace()
            null
        } catch (e: InterruptedException) {
            null
        }
    }
}