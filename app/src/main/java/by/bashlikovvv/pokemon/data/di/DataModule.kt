package by.bashlikovvv.pokemon.data.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.room.Room
import by.bashlikovvv.pokemon.data.local.PokemonDatabase
import by.bashlikovvv.pokemon.data.local.contract.RoomContract
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
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

    private fun providePokemonListRepository(context: Context): PokemonListRepository {
        return PokemonListRepository(
            provideConnectivityManager(context), providePokemonListApi(),
            providePokemonDetailsApi(), providePokemonDatabase(context).pokemonPageDao
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