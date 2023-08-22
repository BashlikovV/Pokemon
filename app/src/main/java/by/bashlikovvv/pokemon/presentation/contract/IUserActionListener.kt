package by.bashlikovvv.pokemon.presentation.contract

import by.bashlikovvv.pokemon.domain.model.PokemonItem

interface IUserActionListener {
    fun onOpen(pokemonItem: PokemonItem)
    fun onSelect(pokemonItem: PokemonItem)
}