package by.bashlikovvv.pokemon.data.remote

import by.bashlikovvv.pokemon.data.remote.response.PokemonPageDto
import by.bashlikovvv.pokemon.utils.Constants.Companion.PAGE_SIZE
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonListApi {

    @GET("pokemon/")
    suspend fun getPokemonList(
        @Query("limit") limit: Int = PAGE_SIZE,
        @Query("offset") offset: Int = 0
    ): Response<PokemonPageDto>
}