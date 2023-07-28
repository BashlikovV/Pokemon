package by.bashlikovvv.pokemon.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.content.ContextCompat
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.data.mapper.PokemonDetailsDtoMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonDtoMapper
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutionException

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

    private suspend fun getBitmapWithGlide(url: String) = withContext(Dispatchers.IO) {
        return@withContext try {
            val  result = Glide.with(DataModule.applicationContext)
                .asBitmap()
                .load(url)
                .transform(CenterCrop())
                .submit()
                .get()

            result ?: R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext)
        } catch (e: ExecutionException) {
            R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext)
        } catch (e: InterruptedException) {
            R.drawable.baseline_error_24.getBitmapFromImage(DataModule.applicationContext)
        }
    }

    private fun Int.getBitmapFromImage(context: Context): Bitmap {
        val db = ContextCompat.getDrawable(context, this)
        val bit = Bitmap.createBitmap(
            db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bit)
        db.setBounds(0, 0, canvas.width, canvas.height)
        db.draw(canvas)

        return bit
    }
}