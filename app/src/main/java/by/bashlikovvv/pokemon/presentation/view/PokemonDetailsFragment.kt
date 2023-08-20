package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.FragmentPokemonDetailsBinding
import by.bashlikovvv.pokemon.domain.model.PokemonDetails
import by.bashlikovvv.pokemon.presentation.adapters.ImagesListAdapter
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonDetailsFragment : Fragment() {

    private var _binding: FragmentPokemonDetailsBinding? = null

    private val binding: FragmentPokemonDetailsBinding get() = _binding!!

    private val viewModel: PokemonDetailsViewModel by viewModels()

    private val adapter = ImagesListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPokemonDetailsBinding.bind(view)

        setUpFragment(requireArguments().getInt(ARG_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setUpFragment(id: Int) {
        binding.progressCircular.visibility = View.VISIBLE
        viewModel.loadDetails(id) { updateActionListener(it) }

        lifecycleScope.launch {
            viewModel.pokemonDetails.collectLatest {
                (requireActivity() as PokemonActivity).apply {
                    supportActionBar?.title = it.name
                }
                binding.pokemonName.text = it.name
                binding.pokemonHeight.text = getString(R.string.height, it.heightInDm.toString())
                binding.pokemonWeight.text = getString(R.string.weight, it.weightInHg.toString())
                it.types.forEach { type -> addType(type) }
                setUpRecyclerView(it)
            }
        }
    }

    private fun setUpRecyclerView(pokemonDetails: PokemonDetails) {
        val images = mutableListOf<String>()
        pokemonDetails.sprites.sprites.forEach { entry ->
            if (entry.value != null) {
                images.add(entry.value!!)
            }
        }
        adapter.setImages(images)
        binding.recyclerView.onFlingListener = null
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = adapter
    }

    private fun getString(@StringRes resId: Int, data: String): String {
        return requireContext().getString(resId, data)
    }

    private fun addType(type: String) {
        val layout = LayoutInflater.from(requireContext())
            .inflate(R.layout.types_layout_item, binding.typesLayout, false)

        val textView = layout.findViewById<TextView>(R.id.type_name)
        textView.text = type.replaceFirstChar { it.uppercaseChar() }

        binding.typesLayout.addView(layout)
    }

    companion object {
        const val ARG_ID = "id"
    }

    private fun updateActionListener(value: Boolean) {
        with(binding.progressCircular) {
            visibility = if (value) {
                binding.setInvisible()
                View.VISIBLE
            } else {
                binding.setVisible()
                View.GONE
            }
        }
    }

    private fun FragmentPokemonDetailsBinding.setInvisible() {
        pokemonHeight.visibility = View.INVISIBLE
        pokemonWeight.visibility = View.INVISIBLE
        pokemonName.visibility = View.INVISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun FragmentPokemonDetailsBinding.setVisible() {
        pokemonHeight.visibility = View.VISIBLE
        pokemonWeight.visibility = View.VISIBLE
        pokemonName.visibility = View.VISIBLE
        recyclerView.visibility = View.VISIBLE
    }
}