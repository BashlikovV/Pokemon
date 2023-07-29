package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.bashlikovvv.pokemon.R
import by.bashlikovvv.pokemon.data.di.DataModule
import by.bashlikovvv.pokemon.databinding.FragmentPokemonDetailsBinding
import by.bashlikovvv.pokemon.domain.model.SpriteNames
import by.bashlikovvv.pokemon.presentation.contract.HasCustomTitle
import by.bashlikovvv.pokemon.presentation.viewmodel.PokemonDetailsViewModel
import by.bashlikovvv.pokemon.utils.viewModelCreator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PokemonDetailsFragment : Fragment(), HasCustomTitle {

    private lateinit var binding: FragmentPokemonDetailsBinding

    private val viewModel: PokemonDetailsViewModel by viewModelCreator {
        PokemonDetailsViewModel(DataModule.providePokemonDetailsUseCase(requireContext())) {
            updateActionListener()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPokemonDetailsBinding.bind(view)

        setUpFragment(requireArguments().getInt(ARG_ID))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setUpFragment(id: Int) {
        binding.progressCircular.visibility = View.VISIBLE
        viewModel.loadDetails(id)

        lifecycleScope.launch {
            viewModel.pokemonDetails.collectLatest {
                binding.pokemonName.text = it.name
                binding.pokemonHeight.text = getString(R.string.height, it.heightInDm.toString())
                binding.pokemonWeight.text = getString(R.string.weight, it.weightInHg.toString())
                binding.pokemonImage.setImageBitmap(it.sprites.sprites[SpriteNames.FrontShiny().name])
                setUpListeners()
            }
        }
    }

    private fun setUpListeners() {
        binding.buttonSwipeLeft.setOnClickListener { SwipeListeners.onLeftWipe() }
        binding.buttonSwipeRight.setOnClickListener { SwipeListeners.onRightSwipe() }
        binding.pokemonImage.setOnScrollChangeListener(SwipeListeners.onScrollChangeListener)
    }

    private fun getString(@StringRes resId: Int, data: String): String {
        return requireContext().getString(resId, data)
    }

    object SwipeListeners {
        fun onLeftWipe() {  }

        fun onRightSwipe() {  }

        val onScrollChangeListener = View.OnScrollChangeListener { p0, p1, p2, p3, p4 ->

        }
    }

    companion object {
        const val ARG_ID = "id"
    }

    private fun updateActionListener() {
        with(binding.progressCircular) {
            visibility = if (visibility == View.VISIBLE) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
    }

    override fun getTitleRes(): Int {
        return R.string.plain_text
    }
}