package by.bashlikovvv.pokemon.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.bashlikovvv.pokemon.databinding.ActivityPokemonBinding

class PokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
    }
}