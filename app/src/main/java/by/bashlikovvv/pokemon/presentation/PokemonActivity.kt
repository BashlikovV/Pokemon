package by.bashlikovvv.pokemon.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.bashlikovvv.pokemon.databinding.ActivityPokemonBinding

class PokemonActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPokemonBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}