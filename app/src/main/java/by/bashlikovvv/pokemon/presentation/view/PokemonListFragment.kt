package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.FragmentPokemonListBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.presentation.adapters.PokemonListAdapter
import by.bashlikovvv.pokemon.presentation.adapters.PokemonLoaderAdapter
import by.bashlikovvv.pokemon.presentation.adapters.UserActionListener
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonListViewModel
import by.bashlikovvv.pokemon.utils.viewModelCreator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment(), UserActionListener  {

    private lateinit var binding: FragmentPokemonListBinding

    private val viewModel: PokemonListViewModel by viewModelCreator {
        PokemonListViewModel(requireContext())
    }

    private val adapter: PokemonListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonListAdapter(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokemonListBinding.bind(view)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        (requireContext() as AppCompatActivity).apply {
            supportActionBar?.title = getText(R.string.app_name)
        }
        binding.pokemonRecyclerView.adapter = adapter.withLoadStateFooter(PokemonLoaderAdapter())

        setUpLoadStateListener()
        submitPagingData()

        adapter.setOnItemClickListener(this)
    }

    private fun setUpLoadStateListener() {
        adapter.addLoadStateListener { state ->
            with(binding) {
                pokemonRecyclerView.isVisible = state.refresh != LoadState.Loading
                progressCircular.isVisible = state.refresh == LoadState.Loading
            }
        }
    }

    private fun submitPagingData() {
        lifecycleScope.launch {
            viewModel.pokemon.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }

    override fun onOpen(pokemonItem: PokemonItem) {
        val args = bundleOf(PokemonDetailsFragment.ARG_ID to pokemonItem.id)
        findNavController().navigate(
            resId = R.id.action_pokemonListFragment_to_pokemonDetailsFragment,
            args = args
        )
    }

    override fun onSelect(pokemonItem: PokemonItem) {
        adapter.selectPokemon(pokemonItem)
    }
}