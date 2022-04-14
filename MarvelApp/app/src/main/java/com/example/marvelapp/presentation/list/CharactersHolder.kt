package com.example.marvelapp.presentation.list

import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.domain.entities.Character

class CharactersHolder(
    private val binding: ItemCharacterBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(character: Character) {
        with(binding) {
            tvName.text = character.name
            ivImage.load(character.imageURI)
        }
    }
}