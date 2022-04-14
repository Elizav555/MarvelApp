package com.example.marvelapp.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.marvelapp.databinding.ItemCharacterBinding
import com.example.marvelapp.domain.entities.Character

class CharactersAdapter() :
    ListAdapter<Character, CharactersHolder>(CharactersDiffItemCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharactersHolder = CharactersHolder(
        ItemCharacterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
    )

    override fun onBindViewHolder(
        holder: CharactersHolder,
        position: Int
    ) {
        val character = getItem(position)
        holder.bind(character)
    }

    override fun submitList(list: List<Character>?) {
        super.submitList(if (list == null) null else ArrayList(list))
    }
}