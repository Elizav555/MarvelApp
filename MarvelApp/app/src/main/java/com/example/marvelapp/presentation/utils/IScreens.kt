package com.example.marvelapp.presentation.utils

import com.github.terrakok.cicerone.Screen

interface IScreens {
    fun listScreen(): Screen
    fun detailScreen(characterId: Int): Screen
}