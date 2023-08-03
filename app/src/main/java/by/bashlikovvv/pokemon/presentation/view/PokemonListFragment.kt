package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.databinding.FragmentPokemonListBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.presentation.adapters.PokemonListAdapter
import by.bashlikovvv.pokemon.presentation.adapters.UserActionListener
import by.bashlikovvv.pokemon.presentation.contract.CustomAction
import by.bashlikovvv.pokemon.presentation.contract.HasCustomAction
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonListViewModel
import by.bashlikovvv.pokemon.utils.viewModelCreator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment(), HasCustomAction {

    private lateinit var binding: FragmentPokemonListBinding

    private val viewModel: PokemonListViewModel by viewModelCreator {
        PokemonListViewModel(DataModule.providePokemonListUseCase(requireContext())) {
            updateActionListener(it)
        }
    }

    private val adapter: PokemonListAdapter by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        PokemonListAdapter(actionListener)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)

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
            /*supportActionBar?.setCustomView()*/
        }
        val layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = VERTICAL
        }
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = adapter
        lifecycleScope.launch {
            viewModel.pokemon.collectLatest {
                adapter.setPokemon(it)
            }
        }
    }

    private val actionListener = object : UserActionListener {
        override fun onOpen(pokemonItem: PokemonItem) {
            val args = bundleOf(PokemonDetailsFragment.ARG_ID to pokemonItem.id)
            this@PokemonListFragment.findNavController().navigate(
                resId = R.id.action_pokemonListFragment_to_pokemonDetailsFragment,
                args = args,
                navOptions = NavOptions.Builder()
                    .setEnterAnim(com.google.android.material.R.anim.abc_tooltip_enter)
                    .setExitAnim(com.google.android.material.R.anim.abc_tooltip_exit)
                    .build()
            )
        }

        override fun onSelect(pokemonItem: PokemonItem) {
            adapter.selectPokemon(pokemonItem)
        }
    }

    private fun updateActionListener(value: Boolean) {
        with(binding.progressCircular) {
            visibility = if (value) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    override fun getCustomAction(): CustomAction = adapter.deleteCustomAction
}