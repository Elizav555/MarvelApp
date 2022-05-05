package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.domain.useCases.GetByIdUseCase
import com.example.marvelapp.presentation.Screens
import com.example.marvelapp.presentation.views.DetailView
import com.github.terrakok.cicerone.Router
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import moxy.MvpPresenter
import javax.inject.Inject

class DetailPresenter @Inject constructor(
    private val getByIdUseCase: GetByIdUseCase,
    private val router: Router
) :
    MvpPresenter<DetailView>() {
    private val disposables = CompositeDisposable()

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    fun getCharacter(characterId: Int) = getByIdUseCase(characterId)
        .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            viewState.showLoading()
        }
        .doAfterSuccess {
            viewState.hideLoading()
        }
        .subscribeBy(onSuccess = {
            viewState.showCharacter(it)
        }, onError = { error ->
            viewState.showError(error.message ?: "error")
        }).addTo(disposables)

    fun navigateToList() {
        router.navigateTo(Screens.listScreen())
    }
}
