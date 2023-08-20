package by.bashlikovvv.pokemon.data.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import by.bashlikovvv.pokemon.data.local.PokemonDatabase
import by.bashlikovvv.pokemon.data.local.contract.RoomContract
import by.bashlikovvv.pokemon.data.local.dao.PokemonDetailsDao
import by.bashlikovvv.pokemon.data.local.dao.PokemonPageDao
import by.bashlikovvv.pokemon.data.local.model.PokemonItemEntity
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.remote.PokemonRemoteMediator
import by.bashlikovvv.pokemon.data.repository.PokemonDetailsRepository
import by.bashlikovvv.pokemon.data.repository.PokemonListRepository
import by.bashlikovvv.pokemon.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, factory: Converter.Factory): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(factory)
        .build()

    @Provides
    @Singleton
    fun providePokemonListApi(retrofit: Retrofit): PokemonListApi {
        return retrofit.create(PokemonListApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDetailsApi(retrofit: Retrofit): PokemonDetailsApi {
        return retrofit.create(PokemonDetailsApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(@ApplicationContext ctx: Context): PokemonDatabase {
        return Room.databaseBuilder(
            ctx,
            PokemonDatabase::class.java,
            RoomContract.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonPageDao(db: PokemonDatabase): PokemonPageDao {
        return db.pokemonPageDao
    }

    @Provides
    @Singleton
    fun providePokemonDetailsDao(db: PokemonDatabase): PokemonDetailsDao {
        return db.pokemonDetailsDao
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PagerOnline
    @Provides
    @Singleton
    @OptIn(ExperimentalPagingApi::class)
    @PagerOnline
    fun providePokemonPagerOnline(
        dao: PokemonPageDao,
        listApi: PokemonListApi,
        detailsApi: PokemonDetailsApi
    ): Pager<Int, PokemonItemEntity> {
        return Pager(
            config = PagingConfig(
                pageSize = Constants.PAGE_SIZE,
                initialLoadSize = Constants.PAGE_SIZE,
                maxSize = Constants.POKEMON_SIZE,
                prefetchDistance = Constants.PAGE_SIZE
            ),
            remoteMediator = PokemonRemoteMediator(
                pokemonListApi = listApi,
                pokemonPageDao = dao,
                pokemonDetailsApi = detailsApi
            )
        ) {
            dao.selectItemsOnline()
        }
    }

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class PagerOffline
    @Provides
    @Singleton
    @PagerOffline
    fun providePokemonPagerOffline(dao: PokemonPageDao): Pager<Int, PokemonItemEntity> {
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

    @Provides
    @Singleton
    fun providePokemonListRepository(
        @PagerOnline pagerOnline: Pager<Int, PokemonItemEntity>,
        @PagerOffline pagerOffline: Pager<Int, PokemonItemEntity>,
        @ApplicationContext context: Context
    ): PokemonListRepository {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return PokemonListRepository(
            cm = connectivityManager,
            pagerOnline = pagerOnline,
            pagerOffline = pagerOffline
        )
    }

    @Provides
    @Singleton
    fun providePokemonDetailsRepository(
        detailsApi: PokemonDetailsApi,
        detailsDao: PokemonDetailsDao,
        @ApplicationContext context: Context
    ): PokemonDetailsRepository {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return PokemonDetailsRepository(
            cm = connectivityManager,
            pokemonDetailsApi = detailsApi,
            pokemonDetailsDao = detailsDao
        )
    }
}