package com.example.marvelapp.data.mappers

import com.example.marvelapp.data.response.CharacterResult
import com.example.marvelapp.domain.entities.Character

class CharactersMapper {
    fun map(result: CharacterResult) =
        Character(result.description, result.id, result.name, result.thumbnail.path)
}