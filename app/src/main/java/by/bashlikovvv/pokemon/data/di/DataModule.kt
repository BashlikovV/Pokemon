package by.bashlikovvv.pokemon.data.di

import android.content.Context
import android.net.ConnectivityManager
import by.bashlikovvv.pokemon.data.remote.PokemonDetailsApi
import by.bashlikovvv.pokemon.data.remote.PokemonListApi
import by.bashlikovvv.pokemon.data.repository.PokemonListRepository
import by.bashlikovvv.pokemon.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataModule {

    private lateinit var applicationContext: Context

    private fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun providePokemonListApi(retrofit: Retrofit = provideRetrofit()): PokemonListApi = retrofit.create(PokemonListApi::class.java)

    fun providePokemonDetailsApi(retrofit: Retrofit = provideRetrofit()): PokemonDetailsApi = retrofit.create(PokemonDetailsApi::class.java)

    private fun provideConnectivityManager(context: Context = applicationContext): ConnectivityManager? {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    }

    fun providePokemonListRepository(): PokemonListRepository {
        return PokemonListRepository(
            applicationContext, providePokemonListApi(), provideConnectivityManager()
        )
    }

    fun init(context: Context) {
        applicationContext = context
    }
}