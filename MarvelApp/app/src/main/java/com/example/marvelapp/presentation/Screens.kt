package com.example.marvelapp.presentation

import com.example.marvelapp.presentation.fragments.DetailFragment
import com.example.marvelapp.presentation.fragments.ListFragment
import com.example.marvelapp.presentation.utils.IScreens
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens : IScreens {
    override fun listScreen(): Screen = FragmentScreen { ListFragment() }

    override fun detailScreen(characterId: Int): Screen =
        FragmentScreen { DetailFragment.newInstance(characterId) }
}
