package com.example.marvelapp.data

import com.example.marvelapp.data.response.CharacterResult
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MarvelApi {
    @GET("v1/public/characters?")
    fun getCharacters(
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("ts") timestamp: Long,
        @Query("hash") hash: String
    ): Single<List<CharacterResult>>

    @GET("v1/public/characters/{id}?")
    fun getCharacterById(
        @Path("id") characterId: Int,
        @Query("ts") timestamp: Long,
        @Query("hash") hash: String
    ): Single<CharacterResult>

    @GET("v1/public/characters?")
    fun getCharactersByName(
        @Query("nameStartsWith") nameStartsWith: String,
        @Query("limit") limit: Int,
        @Query("orderBy") orderBy: String,
        @Query("ts") timestamp: Long,
        @Query("hash") hash: String
    ): Single<List<CharacterResult>>
}
