package by.bashlikovvv.pokemon.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.DetailsNotFoundException
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonDetailsByIdUseCase
import by.bashlikovvv.pokemon.utils.getBitmapFromImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val getPokemonDetailsByIdUseCase: GetPokemonDetailsByIdUseCase,
    @ApplicationContext context: Context
) : ViewModel() {

    private val _pokemonDetails = MutableStateFlow(PokemonDetails())
    val pokemonDetails = _pokemonDetails.asStateFlow()

    private val bm by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        R.drawable.baseline_error_24.getBitmapFromImage(context)
    }

    fun loadDetails(id: Int, updateActionListener: UpdateActionListener) {
        viewModelScope.launch {
            updateActionListener.updateActionListener(true)
            _pokemonDetails.update {
                try {
                    getPokemonDetailsByIdUseCase.getDetails(id)
                } catch (e: DetailsNotFoundException) {
                    val sprites = mapOf(SpriteNames.FrontShiny().name to bm)

                    PokemonDetails(name = e.message ?: "", sprites = Sprites(sprites))
                }
            }
        }.invokeOnCompletion { updateActionListener.updateActionListener(false) }
    }
}

fun interface UpdateActionListener {
    fun updateActionListener(value: Boolean)
}
