package by.bashlikovvv.pokemon.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.ImagesListItemBinding
import com.bumptech.glide.Glide

class ImagesListAdapter : RecyclerView.Adapter<ImagesListAdapter.ImagesListHolder>() {

    private var images = emptyList<String>()

    class ImagesListHolder(
        val binding: ImagesListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImagesListItemBinding.inflate(inflater, parent, false)

        return ImagesListHolder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImagesListHolder, position: Int) {
        val image = images[position]
        with(holder.binding) {
            pokemonImage.tag = image
            setBitmapWithGlide(image, pokemonImage)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    private fun setBitmapWithGlide(url: String, view: ImageView) {
        Glide.with(view)
            .load(url)
            .centerCrop()
            .error(R.drawable.baseline_error_24)
            .into(view)
    }
}