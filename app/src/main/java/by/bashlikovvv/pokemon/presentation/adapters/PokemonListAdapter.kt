package by.bashlikovvv.pokemon.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.PokemonListItemBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.presentation.contract.IUserActionListener
import com.bumptech.glide.Glide

class PokemonListAdapter(context: Context) :
    PagingDataAdapter<PokemonItem, PokemonListAdapter.PokemonListHolder>(PokemonDiffItemCallback) {

    private val layoutInflater = LayoutInflater.from(context)

    private var onItemListener: IUserActionListener? = null

    override fun onBindViewHolder(holder: PokemonListHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListHolder {
        return PokemonListHolder(
            layoutInflater.inflate(R.layout.pokemon_list_item, parent, false)
        )
    }

    inner class PokemonListHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding = PokemonListItemBinding.bind(view)

        fun bind(item: PokemonItem?) {
            if (item == null) return
            if (item.isEmpty()) return
            with(binding) {
                pokemonListItem.tag = item
                pokemonName.text = item.name.replaceFirstChar { it.uppercase() }
                setBitmapWithGlide(item.sprite, pokemonSprite)
                if (item.selected) {
                    selectIndicator.visibility = View.VISIBLE
                } else {
                    selectIndicator.visibility = View.GONE
                }
            }
            itemView.setOnClickListener { onItemListener?.onOpen(item) }
            itemView.setOnLongClickListener {
                onItemListener?.onSelect(item)
                true
            }
        }
    }

    fun setOnItemClickListener(listener: IUserActionListener) {
        onItemListener = listener
    }

    private object PokemonDiffItemCallback : DiffUtil.ItemCallback<PokemonItem>() {
        override fun areItemsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: PokemonItem, newItem: PokemonItem): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private fun setBitmapWithGlide(url: String, view: ImageView) {
        Glide.with(view.context)
            .asBitmap()
            .load(url)
            .centerCrop()
            .into(view)
    }
}