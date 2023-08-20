package by.bashlikovvv.pokemon.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity
import by.bashlikovvv.pokemon.data.mapper.PokemonItemEntityMapper
import by.bashlikovvv.pokemon.data.mapper.PokemonPageDtoMapper
import by.bashlikovvv.pokemon.utils.Constants.Companion.PAGE_SIZE
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator @Inject constructor(
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
                    it.sprite = pokemonDetailsDto.spritesDto.frontShiny!!
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
}