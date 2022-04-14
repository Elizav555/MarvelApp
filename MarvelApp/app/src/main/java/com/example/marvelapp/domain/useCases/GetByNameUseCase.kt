package com.example.marvelapp.domain.useCases

import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.entities.Character
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetByNameUseCase @Inject constructor(
    private val charactersRep: CharactersRep
) {
    operator fun invoke(name: String): Single<List<Character>> =
        charactersRep.getCharactersByName(name, 3).subscribeOn(
            Schedulers.io()
        )
}
