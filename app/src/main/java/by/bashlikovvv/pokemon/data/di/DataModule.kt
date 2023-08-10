package by.bashlikovvv.pokemon.data.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import by.bashlikovvv.pokemon.data.local.PokemonDatabase
import by.bashlikovvv.pokemon.data.local.contract.RoomContract
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.remote.PokemonRemoteMediator
import by.bashlikovvv.pokemon.data.repository.PokemonDetailsRepository
import by.bashlikovvv.pokemon.data.repository.PokemonListRepository
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonDetailsByIdUseCase
import by.bashlikovvv.pokemon.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    private fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun providePokemonListApi(retrofit: Retrofit = provideRetrofit()): PokemonListApi {
        return retrofit.create(PokemonListApi::class.java)
    }

    private fun providePokemonDetailsApi(retrofit: Retrofit = provideRetrofit()): PokemonDetailsApi {
        return retrofit.create(PokemonDetailsApi::class.java)
    }

    private fun provideConnectivityManager(context: Context): ConnectivityManager? {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    }

    private fun providePokemonDatabase(ctx: Context): PokemonDatabase {
        return Room.databaseBuilder(
            ctx,
            PokemonDatabase::class.java,
            RoomContract.DATABASE_NAME
        ).build()
    }

    private fun providePokemonPageDao(db: PokemonDatabase): PokemonPageDao {
        return db.pokemonPageDao
    }

    @OptIn(ExperimentalPagingApi::class)
    fun providePokemonPagerOnline(dao: PokemonPageDao, api: PokemonListApi): Pager<Int, PokemonItemEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.PAGE_SIZE,
                maxSize = Constants.POKEMON_SIZE
            ),
            remoteMediator = PokemonRemoteMediator(
                pokemonListApi = api,
                pokemonPageDao = dao,
                pokemonDetailsApi = providePokemonDetailsApi()
            )
        ) {
            dao.selectItemsOnline()
        }
    }

    private fun providePokemonPagerOffline(dao: PokemonPageDao): Pager<Int, PokemonItemEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.PAGE_SIZE,
                maxSize = Constants.POKEMON_SIZE
            )
        ) {
            dao.selectItemsOffline()
        }
    }

    private fun providePokemonListRepository(context: Context): PokemonListRepository {
        return PokemonListRepository(
            cm = provideConnectivityManager(context),
            pagerOnline = providePokemonPagerOnline(
                providePokemonPageDao(db = providePokemonDatabase(context)),
                providePokemonListApi(provideRetrofit())
            ),
            pagerOffline = providePokemonPagerOffline(
                providePokemonPageDao(db = providePokemonDatabase(context))
            )
        )
    }

    fun providePokemonListUseCase(context: Context): GetPokemonByListUseCase {
        return GetPokemonByListUseCase(providePokemonListRepository(context))
    }

    private fun providePokemonDetailsRepository(context: Context): PokemonDetailsRepository {
        return PokemonDetailsRepository(
            provideConnectivityManager(context), providePokemonDetailsApi(),
            providePokemonDatabase(context).pokemonDetailsDao
        )
    }

    fun providePokemonDetailsUseCase(context: Context): GetPokemonDetailsByIdUseCase {
        return GetPokemonDetailsByIdUseCase(providePokemonDetailsRepository(context))
    }
}