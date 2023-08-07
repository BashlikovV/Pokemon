package by.bashlikovvv.pokemon.data.repository

import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsDtoMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonDtoMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonItemEntityMapper
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import by.bashlikovvv.pokemon.utils.getBitmapFromImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

class PokemonListRepository(
    private val cm: ConnectivityManager?,
    private val pokemonListApi: PokemonListApi,
    private val pokemonDetailsApi: PokemonDetailsApi,
    private val pokemonPageDao: PokemonPageDao
) : IPokemonListRepository {

    private val pokemonDtoMapper = PokemonDtoMapper()

    private val pokemonItemEntityMapper = PokemonItemEntityMapper()

    override suspend fun getList(): List<PokemonItem> {
        return if (isConnected()) {
            getListOnline()
        } else {
            getListOffline()
        }
    }

    private suspend fun getListOnline(): List<PokemonItem> {
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

        withContext(Dispatchers.Default) {
            pokemonPageDao.insertItems(result.map {
                pokemonItemEntityMapper.mapToEntity(it)
            })
        }

        return result
    }

    private suspend fun getListOffline(): List<PokemonItem> {
        return pokemonPageDao.selectItemsOnline().map {
            pokemonItemEntityMapper.mapFromEntity(it)
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

    private suspend fun getBitmapWithGlide(url: String?) = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = Glide.with(DataModule.applicationContext!!)
                .asBitmap()
                .load(url)
                .transform(CenterCrop())
                .submit()
                .get()

            result ?: R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
        } catch (e: ExecutionException) {
            R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
        } catch (e: InterruptedException) {
            R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext!!)
        }
    }
}