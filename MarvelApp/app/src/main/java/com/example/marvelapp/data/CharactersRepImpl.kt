package com.example.marvelapp.data

import com.example.marvelapp.BuildConfig
import com.example.marvelapp.data.mappers.CharactersMapper
import com.example.marvelapp.data.utils.MD5
import com.example.marvelapp.data.utils.Order
import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.entities.Character
import io.reactivex.rxjava3.core.Single
import java.util.*

class CharactersRepImpl(
    private val api: MarvelApi,
    private val charactersMapper: CharactersMapper,
    md5: MD5
) : CharactersRep {
    private val timestamp = Date().time
    private val hash =
        md5(timestamp.toString() + BuildConfig.MARVEL_PRIVATE_KEY + BuildConfig.MARVEL_PUBLIC_KEY)

    override fun getCharacters(limit: Int): Single<List<Character>> =
        api.getCharacters(limit, orderBy = Order.MODIFIED.query, timestamp, hash)
            .map { list -> list.data.results.map { charactersMapper.map(it) } }

    override fun getCharacterById(characterId: Int): Single<Character> =
        api.getCharacterById(characterId, timestamp, hash)
            .map { charactersMapper.map(it.data.results.first()) }

    override fun getCharactersByName(nameStartsWith: String, limit: Int): Single<List<Character>> =
        api.getCharactersByName(nameStartsWith, limit, Order.NAME.query, timestamp, hash)
            .map { list -> list.data.results.map { charactersMapper.map(it) } }
}
