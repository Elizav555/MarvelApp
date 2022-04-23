package com.example.marvelapp.presentation.views

import com.example.marvelapp.domain.entities.Character
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

@AddToEndSingle
interface DetailView : MvpView {
    fun showCharacter(character: Character)
    fun showLoading()
    fun hideLoading()

    @Skip
    fun showError(text: String)
}
