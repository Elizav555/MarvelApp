package com.example.marvelapp.domain

import com.example.marvelapp.domain.entities.Character
import retrofit2.http.GET

interface CharactersRep {
    suspend fun getCharacters(limit: Int): List<Character>

    @GET("characters/{id}")
    suspend fun getCharacterById(characterId: Int): Character

    @GET("characters?orderBy=name")
    suspend fun getCharactersByName(
        nameStartsWith: String,
        limit: Int,
    ): List<Character>
}