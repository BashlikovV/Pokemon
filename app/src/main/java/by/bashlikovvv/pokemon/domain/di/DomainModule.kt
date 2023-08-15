package by.bashlikovvv.pokemon.domain.di

import by.bashlikovvv.pokemon.data.repository.PokemonDetailsRepository
import by.bashlikovvv.pokemon.data.repository.PokemonListRepository
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonDetailsByIdUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun providePokemonListUseCase(pokemonListRepository: PokemonListRepository): GetPokemonByListUseCase {
        return GetPokemonByListUseCase(pokemonListRepository)
    }

    @Provides
    fun providePokemonDetailsUseCase(detailsRepository: PokemonDetailsRepository): GetPokemonDetailsByIdUseCase {
        return GetPokemonDetailsByIdUseCase(detailsRepository)
    }
}