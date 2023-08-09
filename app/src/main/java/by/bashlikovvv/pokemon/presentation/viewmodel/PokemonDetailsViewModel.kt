package by.bashlikovvv.pokemon.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.DetailsNotFoundException
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.domain.model.Sprites
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonDetailsByIdUseCase
import by.bashlikovvv.pokemon.utils.getBitmapFromImage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonDetailsViewModel(
    private val getPokemonDetailsByIdUseCase: GetPokemonDetailsByIdUseCase
) : ViewModel() {

    private val _pokemonDetails = MutableStateFlow(PokemonDetails())
    val pokemonDetails = _pokemonDetails.asStateFlow()

    fun loadDetails(id: Int, updateActionListener: UpdateActionListener) {
        viewModelScope.launch {
            updateActionListener.invoke(true)
            _pokemonDetails.update {
                try {
                    getPokemonDetailsByIdUseCase.getDetails(id)
                } catch (e: DetailsNotFoundException) {
                    val bm = R.drawable.baseline_error_24.getBitmapFromImage()
                    val sprites = mapOf(SpriteNames.FrontShiny().name to bm)

                    PokemonDetails(name = e.message ?: "", sprites = Sprites(sprites))
                }
            }
        }.invokeOnCompletion { updateActionListener.invoke(false) }
    }
}