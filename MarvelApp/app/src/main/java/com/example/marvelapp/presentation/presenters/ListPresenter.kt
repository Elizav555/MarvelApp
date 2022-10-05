package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.domain.useCases.GetByNameUseCase
import com.example.marvelapp.domain.useCases.GetCharactersUseCase
import com.example.marvelapp.presentation.views.CharactersListView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import moxy.MvpPresenter
import javax.inject.Inject

class ListPresenter @Inject constructor(
    private val getByNameUseCase: GetByNameUseCase,
    private val getCharactersUseCase: GetCharactersUseCase
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

    fun onCharactersSearch(name: String) = getByNameUseCase(name)
        .observeOn(AndroidSchedulers.mainThread())
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

    private fun getCharacters() = getCharactersUseCase()
        .observeOn(AndroidSchedulers.mainThread())
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
