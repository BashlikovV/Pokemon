package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonDetailsByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val getPokemonDetailsByIdUseCase: GetPokemonDetailsByIdUseCase,
    private val updateActionListener: UpdateActionListener
) : ViewModel() {

    private val _pokemonDetails = MutableStateFlow(PokemonDetails())
    val pokemonDetails = _pokemonDetails.asStateFlow()

    fun loadDetails(id: Int) {
        viewModelScope.launch {
            updateActionListener.invoke()
            _pokemonDetails.update { getPokemonDetailsByIdUseCase.getDetails(id) }
        }.invokeOnCompletion { updateActionListener.invoke() }
    }
}