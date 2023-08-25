package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.databinding.FragmentPokemonListBinding
import by.bashlikovvv.pokemon.domain.model.PokemonItem
import by.bashlikovvv.pokemon.presentation.adapters.PokemonListAdapter
import by.bashlikovvv.pokemon.presentation.adapters.PokemonLoaderAdapter
import by.bashlikovvv.pokemon.presentation.contract.IUserActionListener
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PokemonListFragment : Fragment(), IUserActionListener {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding: FragmentPokemonListBinding get() = _binding!!

    private val viewModel: PokemonListViewModel by viewModels()

    private val adapter: PokemonListAdapter by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
        PokemonListAdapter(requireContext())
    }

    private var _searchView: SearchView? = null
    private val searchView get() = _searchView!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentPokemonListBinding.bind(view)

        setUpFragment()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
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

    private fun setUpFragment() {
        (requireActivity() as PokemonActivity).apply {
            supportActionBar?.title = getText(R.string.app_name)
            onBackPressedDispatcher.addCallback(
                owner = viewLifecycleOwner,
                onBackPressedCallback = onBackPressedCallback()
            )
        }
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider(), viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onBackPressedCallback() = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (viewModel.isContainsSelected()) {
                viewModel.unselectPokemon()
            } else if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
            } else {
                requireActivity().finish()
            }
        }
    }

    private fun menuProvider() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.search_menu, menu)
            val item: MenuItem? = menu.findItem(R.id.action_search)
            if (item != null) {
                _searchView = item.actionView as SearchView
                setUpSearchView(searchView)
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_search -> {
                    viewModel.unselectPokemon()
                    true
                }
                else -> { false }
            }
        }
    }

    private fun setUpSearchView(menuItem: SearchView) {
        with(menuItem) {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.onSearchTextChange(query ?: "")
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onSearchTextChange(newText ?: "")
                    return true
                }
            })
            setOnSearchClickListener {
                viewModel.unselectPokemon()
            }
        }
    }

    override fun onOpen(pokemonItem: PokemonItem) {
        if (viewModel.isContainsSelected()) {
            viewModel.selectPokemon(pokemonItem)
        } else {
            val args = bundleOf(PokemonDetailsFragment.ARG_ID to pokemonItem.id)
            findNavController().navigate(
                resId = R.id.action_pokemonListFragment_to_pokemonDetailsFragment,
                args = args
            )
        }
    }

    override fun onSelect(pokemonItem: PokemonItem) = viewModel.selectPokemon(pokemonItem)

    override fun onStop() {
        viewModel.unselectPokemon()
        super.onStop()
    }
}