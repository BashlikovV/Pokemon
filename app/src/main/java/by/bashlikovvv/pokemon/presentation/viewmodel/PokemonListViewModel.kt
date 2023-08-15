package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    getPokemonByListUseCase: GetPokemonByListUseCase
) : ViewModel() {

    val pokemon = getPokemonByListUseCase.execute()
}