package com.example.marvelapp.domain.useCases

import com.example.marvelapp.domain.CharactersRep
import javax.inject.Inject

class GetByNameUseCase @Inject constructor(
    private val charactersRep: CharactersRep
) {
    suspend operator fun invoke(name: String) = charactersRep.getCharactersByName(name, 10)
}
