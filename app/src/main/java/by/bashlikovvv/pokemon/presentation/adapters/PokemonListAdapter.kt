package by.bashlikovvv.pokemon.presentation.adapters

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.PokemonListItemBinding
import by.bashlikovvv.pokemon.domain.model.Pokemon

interface UserActionListener {

    fun onOpen(pokemon: Pokemon)
}

class PokemonListAdapter(
    private val actionListener: UserActionListener
) : RecyclerView.Adapter<PokemonListAdapter.PokemonListHolder>(), View.OnClickListener {

    var pokemon: List<Pokemon> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var startAnimator = ObjectAnimator()

    private var stopAnimator = ObjectAnimator()

    var selectedItem = 0
        set(value) {
            field = value
            notifyItemChanged(field)
            stopAnimator.start()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PokemonListItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.root.animate().withEndAction {
            binding.decLayoutSize()
        }.withStartAction {
            binding.incLayoutSize()
        }

        return PokemonListHolder(binding)
    }

    override fun getItemCount(): Int = pokemon.size

    override fun onBindViewHolder(holder: PokemonListHolder, position: Int) {
        val item = pokemon[position]
        with(holder.binding) {
            pokemonListItem.tag = item

            if (selectedItem == item.id.toInt()) {
                setIncSpriteAnimation()
                setDecSpriteAnimation()
                startAnimator.start()
            }

            pokemonSprite.setImageBitmap(item.sprite)
            pokemonName.text = item.name
        }
    }

    private fun PokemonListItemBinding.setIncSpriteAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat(
            View.SCALE_X, 1f, 2f
        )
        val scaleY = PropertyValuesHolder.ofFloat(
            View.SCALE_Y, 1f, 2f
        )
        val translationY = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_Y, 1f, -100f
        )
        startAnimator = ObjectAnimator.ofPropertyValuesHolder(
            pokemonSprite,
            scaleX, scaleY, translationY
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = 0
        }
    }

    private fun PokemonListItemBinding.setDecSpriteAnimation() {
        val scaleX = PropertyValuesHolder.ofFloat(
            View.SCALE_X, 2f, 1f
        )
        val scaleY = PropertyValuesHolder.ofFloat(
            View.SCALE_Y, 2f, 1f
        )
        val translationY = PropertyValuesHolder.ofFloat(
            View.TRANSLATION_Y, -100f, 1f
        )
        stopAnimator = ObjectAnimator.ofPropertyValuesHolder(
            pokemonSprite,
            scaleX, scaleY, translationY
        ).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            repeatCount = 0
            repeatMode = ObjectAnimator.REVERSE
        }
    }

    private fun PokemonListItemBinding.incLayoutSize() {
        val newHeight = root.height * 2
        val newWidth = root.width * 2

        root.layoutParams.apply {
            height = newHeight
            width = newWidth
        }

        notifyItemChanged(selectedItem)
    }

    private fun PokemonListItemBinding.decLayoutSize() {
        val newHeight = root.height / 2
        val newWidth = root.width / 2

        root.layoutParams.apply {
            height = newHeight
            width = newWidth
        }

        notifyItemChanged(selectedItem)
    }

    class PokemonListHolder(
        val binding: PokemonListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onClick(v: View) {
        val item = v.tag as Pokemon
        when (v.id) {
            R.id.pokemon_list_item -> {
                actionListener.onOpen(item)
            }
        }
    }
}