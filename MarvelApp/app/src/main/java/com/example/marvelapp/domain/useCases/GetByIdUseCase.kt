package com.example.marvelapp.domain.useCases

import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.entities.Character
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class GetByIdUseCase @Inject constructor(
    private val charactersRep: CharactersRep
) {
    operator fun invoke(id: Int): Single<Character> =
        charactersRep.getCharacterById(id).subscribeOn(
            Schedulers.io()
        )
}
