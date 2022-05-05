package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.domain.useCases.GetByNameUseCase
import com.example.marvelapp.domain.useCases.GetCharactersUseCase
import com.example.marvelapp.presentation.Screens
import com.example.marvelapp.presentation.views.CharactersListView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import moxy.MvpPresenter
import javax.inject.Inject

class ListPresenter @Inject constructor(
    private val getByNameUseCase: GetByNameUseCase,
    private val getCharactersUseCase: GetCharactersUseCase,
    private val router: Router,
    private val scheduler: Scheduler
) : MvpPresenter<CharactersListView>() {
    private val disposables = CompositeDisposable()

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getCharacters()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    fun navigateToDetails(characterId: Int) {
        router.navigateTo(Screens.detailScreen(characterId))
    }

    fun onCharactersSearch(name: String) = getByNameUseCase(name)
        .observeOn(scheduler)
        .doOnSubscribe {
            viewState.showLoading()
        }
        .doAfterSuccess {
            viewState.hideLoading()
        }
        .subscribeBy(onSuccess = {
            viewState.updateList(it)
        }, onError = { error ->
            viewState.showError(error.message ?: "error")
        }).addTo(disposables)

    fun getCharacters() = getCharactersUseCase()
        .observeOn(scheduler)
        .doOnSubscribe {
            viewState.showLoading()
        }
        .doAfterSuccess {
            viewState.hideLoading()
        }
        .subscribeBy(onSuccess = {
            viewState.updateList(it)
        }, onError = { error ->
            viewState.showError(error.message ?: "error")
        }).addTo(disposables)
}
