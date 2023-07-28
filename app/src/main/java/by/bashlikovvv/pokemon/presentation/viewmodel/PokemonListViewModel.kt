package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonListViewModel(
    getPokemonByListUseCase: GetPokemonByListUseCase
) : ViewModel() {

    private val _pokemon: MutableStateFlow<List<PokemonItem>> = MutableStateFlow(emptyList())
    val pokemon = _pokemon.asStateFlow()

    init {
        viewModelScope.launch {
            _pokemon.update { getPokemonByListUseCase.execute() }
        }
    }
}