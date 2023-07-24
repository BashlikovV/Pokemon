package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase

class PokemonListViewModel(
    getPokemonByListUseCase: GetPokemonByListUseCase
) : ViewModel() {

    val pokemon = getPokemonByListUseCase.execute()
}