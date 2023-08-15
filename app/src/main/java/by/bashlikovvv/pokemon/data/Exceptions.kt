package by.bashlikovvv.pokemon.data

import java.lang.RuntimeException

sealed class AppException : RuntimeException()

class DetailsNotFoundException : AppException()