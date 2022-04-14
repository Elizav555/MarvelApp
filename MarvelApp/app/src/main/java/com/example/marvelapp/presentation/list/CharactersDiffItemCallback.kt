package com.example.marvelapp.presentation.list

import androidx.recyclerview.widget.DiffUtil
import com.example.marvelapp.domain.entities.Character

class CharactersDiffItemCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(
        oldItem: Character,
        newItem: Character
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Character,
        newItem: Character
    ): Boolean = oldItem == newItem
}
