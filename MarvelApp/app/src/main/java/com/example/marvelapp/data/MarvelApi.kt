package com.example.marvelapp.data

import com.example.marvelapp.data.response.CharacterResult
import com.example.marvelapp.data.utils.Order
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {
    @GET("characters?")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: Order
    ): List<CharacterResult>

    @GET("characters/{id}")
    suspend fun getCharacterById(@Path("id") characterId: Int): CharacterResult

    @GET("characters?")
    suspend fun getCharactersByName(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: Order
    ): List<CharacterResult>
}
