package by.bashlikovvv.pokemon.data.remote

import android.graphics.Bitmap
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity
import by.bashlikovvv.pokemon.data.mapper.PokemonItemEntityMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonPageDtoMapper
import by.bashlikovvv.pokemon.data.remote.response.SpritesDto
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.presentation.App
import by.bashlikovvv.pokemon.utils.Constants.Companion.PAGE_SIZE
import by.bashlikovvv.pokemon.utils.getBitmapFromImage
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.ExecutionException

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator(
    private val pokemonListApi: PokemonListApi,
    private val pokemonDetailsApi: PokemonDetailsApi,
    private val pokemonPageDao: PokemonPageDao
) : RemoteMediator<Int, PokemonItemEntity>() {

    private val pokemonPageDtoMapper = PokemonPageDtoMapper()
    private val pokemonItemEntityMapper = PokemonItemEntityMapper()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonItemEntity>
    ): MediatorResult {
        val offset = getPageIndex(loadType, state) ?: return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val response = pokemonListApi.getPokemonList(PAGE_SIZE, offset)
            if (response.isSuccessful) {
                val pokemon = pokemonPageDtoMapper.mapFromEntity(response.body()!!).onEach {
                    val pokemonDetailsDto = pokemonDetailsApi.getPokemonDetailsById(it.id).body()!!
                    val sprites = pokemonDetailsDto.spritesDto.getSprites()
                    it.sprites.putAll(sprites.sprites)
                }
                pokemonPageDao.insertItems(pokemon.map { pokemonItemEntityMapper.mapToEntity(it) })
                MediatorResult.Success(endOfPaginationReached = pokemon.size < PAGE_SIZE)
            } else {
                MediatorResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(
        loadType: LoadType,
        state: PagingState<Int, PokemonItemEntity>
    ): Int? = when (loadType) {
        LoadType.REFRESH -> 0
        LoadType.APPEND -> state.lastItemOrNull()?.id ?: 0
        LoadType.PREPEND -> null
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    private suspend fun SpritesDto.getSprites(): Sprites {
        val sprites = mutableMapOf<String, Bitmap?>()
        sprites[SpriteNames.FrontShiny().name] = getBitmapWithGlide(frontShiny)

        return Sprites(sprites)
    }

    private suspend fun getBitmapWithGlide(url: String?) = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = Glide.with(App.instance)
                .asBitmap()
                .load(url)
                .transform(CenterCrop())
                .submit()
                .get()

            result ?: R.drawable.baseline_error_24.getBitmapFromImage(App.instance)
        } catch (e: ExecutionException) {
            R.drawable.baseline_error_24.getBitmapFromImage(App.instance)
        } catch (e: InterruptedException) {
            R.drawable.baseline_error_24.getBitmapFromImage(App.instance)
        }
    }
}