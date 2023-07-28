package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.databinding.FragmentPokemonListBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.presentation.adapters.PokemonListAdapter
import by.bashlikovvv.pokemon.presentation.adapters.UserActionListener
import by.bashlikovvv.pokemon.presentation.contract.CustomAction
import by.bashlikovvv.pokemon.presentation.contract.HasCustomAction
import by.bashlikovvv.pokemon.presentation.contract.HasCustomTitle
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonListViewModel
import by.bashlikovvv.pokemon.utils.viewModelCreator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonListFragment : Fragment(), HasCustomTitle, HasCustomAction {

    private lateinit var binding: FragmentPokemonListBinding

    private val pokemonItemListViewModel: PokemonListViewModel by viewModelCreator {
        PokemonListViewModel(DataModule.providePokemonListUseCase(requireContext()))
    }
    private val adapter: PokemonListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        PokemonListAdapter(actionListener)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPokemonListBinding.inflate(inflater, container, false)

        setUpRecyclerView()

        return binding.root
    }

    private fun setUpRecyclerView() {
        lifecycleScope.launch {
            pokemonItemListViewModel.pokemon.collectLatest {
                adapter.setPokemon(it)
            }
        }
        val layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = VERTICAL
        }
        val snapHelper = LinearSnapHelper()
        binding.pokemonRecyclerView.layoutManager = layoutManager
        binding.pokemonRecyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(binding.pokemonRecyclerView)
    }

    private val actionListener = object : UserActionListener {
        override fun onOpen(pokemonItem: PokemonItem) {
            Toast.makeText(requireContext(), pokemonItem.name, Toast.LENGTH_SHORT).show()
        }

        override fun onSelect(pokemonItem: PokemonItem) {
            adapter.selectPokemon(pokemonItem)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = PokemonListFragment()
    }

    override fun getTitleRes(): Int = R.string.app_name

    override fun getCustomAction(): CustomAction = adapter.deleteCustomAction
}