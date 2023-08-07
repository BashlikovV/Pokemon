package by.bashlikovvv.pokemon.data

import java.lang.RuntimeException

sealed class AppException : RuntimeException {
    constructor() : super()
    constructor(cause: Throwable) : super(cause)
}

class DetailsNotFoundException : AppException()