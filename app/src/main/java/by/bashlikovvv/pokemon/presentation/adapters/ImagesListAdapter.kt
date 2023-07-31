package by.bashlikovvv.pokemon.presentation.adapters

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.pokemon.databinding.ImagesListItemBinding

class ImagesListAdapter : RecyclerView.Adapter<ImagesListAdapter.ImagesListHolder>() {

    private var images = emptyList<Bitmap>()

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
            pokemonImage.setImageBitmap(image)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(images: List<Bitmap>) {
        this.images = images
        notifyDataSetChanged()
    }
}