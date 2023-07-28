package by.bashlikovvv.pokemon.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.PokemonListItemBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.presentation.contract.CustomAction

interface UserActionListener {

    fun onOpen(pokemonItem: PokemonItem)

    fun onSelect(pokemonItem: PokemonItem)
}

class PokemonListAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<PokemonListAdapter.PokemonListHolder>(), View.OnClickListener, View.OnLongClickListener {

    private var pokemon: List<PokemonItem> = emptyList()

    private var selectedPokemonItem: MutableMap<PokemonItem, Boolean> = mutableMapOf()

    val deleteCustomAction = CustomAction(
        iconRes = R.drawable.baseline_delete_outline_24,
        textRes = R.string.remove
    ) { removePokemon() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonListItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.root.setOnLongClickListener(this)

        return PokemonListHolder(binding)
    }

    override fun getItemCount(): Int = pokemon.size

    override fun onBindViewHolder(holder: PokemonListHolder, position: Int) {
        val item = pokemon[position]
        with(holder.binding) {
            pokemonListItem.tag = item
            pokemonSprite.setImageBitmap(item.sprites[SpriteNames.FrontShiny().name])
            pokemonName.text = item.name
            if (selectedPokemonItem[item] == true) {
                selectIndicator.visibility = View.VISIBLE
            } else {
                selectIndicator.visibility = View.INVISIBLE
            }
        }
    }

    class PokemonListHolder(
        val binding: PokemonListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val item = v.tag as PokemonItem
        when (v.id) {
            R.id.pokemon_list_item -> {
                actionListener.onOpen(item)
            }
        }
    }

    fun setPokemon(pokemonItem: List<PokemonItem>) {
        val diffCallback = PokemonDiffCallback(
            oldList = this.pokemon, newList = pokemonItem
        )
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.pokemon = pokemonItem
        diffResult.dispatchUpdatesTo(this)
    }

    fun selectPokemon(pokemonItem: PokemonItem) {
        selectedPokemonItem.merge(pokemonItem, selectedPokemonItem[pokemonItem] ?: true) { _, _ ->
            selectedPokemonItem[pokemonItem]?.not() ?: true
        }
    }

    override fun onLongClick(v: View): Boolean {
        val item = v.tag as PokemonItem
        return when (v.id) {
            R.id.pokemon_list_item -> {
                actionListener.onSelect(item)
                notifyItemChanged(pokemon.indexOf(item))
                true
            }
            else -> { false }
        }
    }

    private fun removePokemon() {
        val tmp = pokemon.toMutableList()
        tmp.removeAll(selectedPokemonItem.map { it.key })
        selectedPokemonItem = mutableMapOf()
        val diffCallback = PokemonDiffCallback(oldList = this.pokemon, newList = tmp)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.pokemon = tmp
        diffResult.dispatchUpdatesTo(this)
    }
}