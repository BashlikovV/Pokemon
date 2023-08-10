package by.bashlikovvv.pokemon.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.PokemonListItemBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames

interface UserActionListener {

    fun onOpen(pokemonItem: PokemonItem)

    fun onSelect(pokemonItem: PokemonItem)
}

class PokemonListAdapter(context: Context) :
    PagingDataAdapter<PokemonItem, PokemonListAdapter.PokemonListHolder>(PokemonDiffItemCallback) {

    private val layoutInflater = LayoutInflater.from(context)
    private var onItemListener: UserActionListener? = null

    private var selectedPokemonItem: MutableMap<PokemonItem, Boolean> = mutableMapOf()

    override fun onBindViewHolder(holder: PokemonListHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListHolder {
        return PokemonListHolder(layoutInflater.inflate(R.layout.pokemon_list_item, parent, false))
    }

    inner class PokemonListHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding = PokemonListItemBinding.bind(view)

        fun bind(item: PokemonItem?) {
            if (item == null) return
            with(binding) {
                pokemonListItem.tag = item
                pokemonName.text = item.name.replaceFirstChar { it.uppercase() }
                pokemonSprite.setImageBitmap(item.sprites[SpriteNames.FrontShiny().name])
                if (selectedPokemonItem[item] == true) {
                    selectIndicator.visibility = View.VISIBLE
                } else {
                    selectIndicator.visibility = View.INVISIBLE
                }
            }
            itemView.setOnClickListener {
                if (selectedPokemonItem.containsValue(true)) {
                    onItemListener?.onSelect(item)
                } else {
                    selectedPokemonItem.clear()
                    onItemListener?.onOpen(item)
                }
            }
            itemView.setOnLongClickListener {
                onItemListener?.onSelect(item)
                true
            }
        }
    }

    private object PokemonDiffItemCallback : DiffUtil.ItemCallback<PokemonItem>() {
        override fun areItemsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }

    fun selectPokemon(pokemonItem: PokemonItem) {
        selectedPokemonItem.merge(pokemonItem, selectedPokemonItem[pokemonItem] ?: true) { _, _ ->
            selectedPokemonItem[pokemonItem]?.not() ?: true
        }
        notifyItemChanged(pokemonItem.id - 1)
    }

    fun setOnItemClickListener(listener: UserActionListener) {
        onItemListener = listener
    }
}