package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

fun interface UpdateActionListener {
    fun invoke(value: Boolean)
}

class PokemonListViewModel(
    getPokemonByListUseCase: GetPokemonByListUseCase,
    updateActionListener: UpdateActionListener
) : ViewModel() {

    private val _pokemon: MutableStateFlow<List<PokemonItem>> = MutableStateFlow(emptyList())
    val pokemon = _pokemon.asStateFlow()

    init {
        viewModelScope.launch {
            updateActionListener.invoke(true)
            _pokemon.update { getPokemonByListUseCase.execute() }
        }.invokeOnCompletion { updateActionListener.invoke(false) }
    }
}