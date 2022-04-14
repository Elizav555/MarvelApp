package com.example.marvelapp.data

import com.example.marvelapp.data.mappers.CharactersMapper
import com.example.marvelapp.data.utils.Order
import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.entities.Character

class CharactersRepImpl(
    private val api: MarvelApi,
    private val charactersMapper: CharactersMapper
) : CharactersRep {
    override suspend fun getCharacters(limit: Int): List<Character> =
        api.getCharacters(limit, orderBy = Order.MODIFIED).map { charactersMapper.map(it) }

    override suspend fun getCharacterById(characterId: Int) =
        charactersMapper.map(api.getCharacterById(characterId))

    override suspend fun getCharactersByName(nameStartsWith: String, limit: Int) =
        api.getCharactersByName(nameStartsWith, limit, Order.NAME).map { charactersMapper.map(it) }
}
