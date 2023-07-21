package by.bashlikovvv.pokemon.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.PokemonListItemBinding
import by.bashlikovvv.pokemon.domain.model.Pokemon
import com.bumptech.glide.Glide

class PokemonListAdapter : RecyclerView.Adapter<PokemonListAdapter.PokemonListHolder>() {

    var pokemon: List<Pokemon> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonListItemBinding.inflate(inflater)
        return PokemonListHolder(binding)
    }

    override fun getItemCount(): Int = pokemon.size

    override fun onBindViewHolder(holder: PokemonListHolder, position: Int) {
        val item = pokemon[position]
        with(holder.binding) {
            setPokemonImage(item)
            pokemonName.text = item.pokemonName
        }
    }

    private fun PokemonListItemBinding.setPokemonImage(item: Pokemon) {
        Glide.with(this.pokemonImage.context)
            .load("TODO(item.pokemonUrl)")
            .circleCrop()
            .placeholder(R.drawable.baseline_error_24)
            .error(R.drawable.baseline_error_24)
            .into(this.pokemonImage)
    }

    class PokemonListHolder(
        val binding: PokemonListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}