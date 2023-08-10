package by.bashlikovvv.pokemon.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase

class PokemonListViewModel(
    context: Context,
    getPokemonByListUseCase: GetPokemonByListUseCase = DataModule.providePokemonListUseCase(context)
) : ViewModel() {

    val pokemon = getPokemonByListUseCase.execute()
}