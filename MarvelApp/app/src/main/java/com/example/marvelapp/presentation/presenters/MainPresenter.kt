package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.presentation.Screens
import com.example.marvelapp.presentation.views.MainView
import com.github.terrakok.cicerone.Router
import moxy.MvpPresenter
import javax.inject.Inject

class MainPresenter @Inject constructor(private val router: Router) : MvpPresenter<MainView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        router.replaceScreen(Screens.listScreen())
    }
}