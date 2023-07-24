package by.bashlikovvv.pokemon.presentation.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.FragmentPokemonListBinding
import by.bashlikovvv.pokemon.domain.model.Pokemon
import by.bashlikovvv.pokemon.domain.repository.IPokemonListRepository
import by.bashlikovvv.pokemon.domain.usecase.GetPokemonByListUseCase
import by.bashlikovvv.pokemon.presentation.adapters.PokemonListAdapter
import by.bashlikovvv.pokemon.presentation.adapters.SnapOnScrollListener
import by.bashlikovvv.pokemon.presentation.adapters.UserActionListener
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonListViewModel
import by.bashlikovvv.pokemon.utils.viewModelCreator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding

    private val pokemonListViewModel: PokemonListViewModel by viewModelCreator {
        PokemonListViewModel(GetPokemonByListUseCase(
            object : IPokemonListRepository {
                override fun getList(): Flow<List<Pokemon>> {
                    val bm = R.drawable.image8.getBitmapFromImage(requireContext())
                    val tmp = (0..30).map {
                        Pokemon(
                            name = "pokemon$it",
                            id = it.toLong(),
                            image = bm,
                            sprite = bm
                        )
                    }
                    return flowOf(tmp)
                }
            }
        ))
    }
    private val adapter: PokemonListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonListAdapter(actionListener)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)

        lifecycleScope.launch { setUpRecyclerView() }

        return binding.root
    }

    private suspend fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = HORIZONTAL
        }
        val snapHelper = LinearSnapHelper()
        val snapOnScrollListener = SnapOnScrollListener(snapHelper) { position ->
            adapter.selectedItem = position
        }
        pokemonListViewModel.pokemon.collectLatest {
            adapter.pokemon = it
        }
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(binding.pokemonRecyclerView)
        binding.pokemonRecyclerView.addOnScrollListener(snapOnScrollListener)
    }

    private fun Int.getBitmapFromImage(context: Context): Bitmap {
        val db = ContextCompat.getDrawable(context, this)
        val bit = Bitmap.createBitmap(
            db!!.intrinsicWidth, db.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bit)
        db.setBounds(0, 0, canvas.width, canvas.height)
        db.draw(canvas)

        return bit
    }

    private val actionListener = object : UserActionListener {
        override fun onOpen(pokemon: Pokemon) {
            Toast.makeText(requireContext(), pokemon.name, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PokemonListFragment()
    }
}