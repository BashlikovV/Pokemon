package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByNameUseCase
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonListUseCase
import by.bashlikovvv.pokemon.domain.usecase.UnselectPokemonUseCase
import by.bashlikovvv.pokemon.domain.usecase.UpdatePokemonUseCase
import by.bashlikovvv.pokemon.presentation.contract.LocalChanges
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class PokemonListViewModel @Inject constructor(
    getPokemonListUseCase: GetPokemonListUseCase,
    getPokemonByNameUseCase: GetPokemonByNameUseCase,
    private val updatePokemonUseCase: UpdatePokemonUseCase,
    private val unselectPokemonUseCase: UnselectPokemonUseCase
) : ViewModel() {

    private var _pokemon: Flow<PagingData<PokemonItem>>
    val pokemon get() = _pokemon

    private val _searchBy: MutableStateFlow<String> = MutableStateFlow("")

    private val localChanges = LocalChanges()
    private val localChangesFlow = MutableStateFlow(OnChange(localChanges))

    init {
        val originPokemonFlow = _searchBy.asStateFlow()
            .debounce(500)
            .flatMapLatest {
                if (it.isEmpty()) {
                    getPokemonListUseCase.execute()
                } else {
                    getPokemonByNameUseCase.execute(it)
                }
            }.cachedIn(viewModelScope)

        _pokemon = combine(
            originPokemonFlow,
            localChangesFlow,
            this::merge
        )
    }

    fun selectPokemon(pokemonItem: PokemonItem) {
        if (localChanges.isInProgress(pokemonItem.id)) return

        viewModelScope.launch {
            try {
                localChanges.selectItem(pokemonItem.id)
                updatePokemonUseCase.execute(pokemonItem.id, !pokemonItem.selected)
            } finally {

                localChangesFlow.update { OnChange(localChanges) }
            }
        }
    }

    fun onSearchTextChange(value: String) = _searchBy.update { value }

    fun unselectPokemon() = viewModelScope.launch {
        try {
            localChanges.unselect()
            unselectPokemonUseCase.execute()
        } finally {
            localChangesFlow.update { OnChange(localChanges) }
        }
    }

    fun isContainsSelected() = localChanges.isContainsSelected()

    private fun merge(users: PagingData<PokemonItem>, localChanges: OnChange<LocalChanges>): PagingData<PokemonItem> {
        return users.map { pokemon ->
                val localSelectedFlag = localChanges.value.isSelected(pokemon.id)

                val userWithLocalChanges = if (localSelectedFlag == null) {
                    pokemon
                } else {
                    pokemon.copy(selected = localSelectedFlag)
                }
                userWithLocalChanges
            }
    }

    class OnChange<T>(val value: T)
}