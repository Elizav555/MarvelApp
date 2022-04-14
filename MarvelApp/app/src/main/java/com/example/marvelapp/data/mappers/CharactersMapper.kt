package com.example.marvelapp.data.mappers

import com.example.marvelapp.data.response.Res
import com.example.marvelapp.domain.entities.Character

class CharactersMapper {
    fun map(result: Res) =
        Character(
            result.description,
            result.id,
            result.name,
            result.thumbnail.path +"."+ result.thumbnail.extension
        )
}