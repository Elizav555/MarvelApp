package com.example.marvelapp.presentation.views

import com.example.marvelapp.domain.entities.Character
import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip

@AddToEndSingle
interface CharactersListView : MvpView {
    fun showLoading()
    fun hideLoading()
    fun updateList(characters: List<Character>)

    @Skip
    fun showError(text: String)

    @OneExecution
    fun navigateToDetail(characterId: Int)
}
