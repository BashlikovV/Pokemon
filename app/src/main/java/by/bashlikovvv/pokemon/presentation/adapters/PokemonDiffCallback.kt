package by.bashlikovvv.pokemon.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import by.bashlikovvv.pokemon.domain.model.PokemonItem

class PokemonDiffCallback(
    private val oldList: List<PokemonItem>,
    private val newList: List<PokemonItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPokemon = oldList[oldItemPosition]
        val newPokemon = newList[newItemPosition]

        return oldPokemon.id == newPokemon.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPokemon = oldList[oldItemPosition]
        val newPokemon = newList[newItemPosition]

        return oldPokemon == newPokemon
    }
}