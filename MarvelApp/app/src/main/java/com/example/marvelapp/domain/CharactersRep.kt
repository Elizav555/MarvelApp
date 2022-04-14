package com.example.marvelapp.domain

import com.example.marvelapp.domain.entities.Character
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface CharactersRep {
    fun getCharacters(limit: Int): Single<List<Character>>

    @GET("characters/{id}")
    fun getCharacterById(characterId: Int): Single<Character>

    @GET("characters?orderBy=name")
    fun getCharactersByName(
        nameStartsWith: String,
        limit: Int,
    ): Single<List<Character>>
}
