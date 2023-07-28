package by.bashlikovvv.pokemon.presentation.contract

import androidx.annotation.StringRes

interface HasCustomTitle {

    @StringRes
    fun getTitleRes(): Int
}