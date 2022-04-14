package com.example.marvelapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.GetByNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val getByNameUseCase: GetByNameUseCase) :
    ViewModel() {
    private var _charactersList: MutableLiveData<Result<List<Character>>> = MutableLiveData()
    val charactersList: LiveData<Result<List<Character>>> = _charactersList

    private val disposables = CompositeDisposable()

    fun onCharactersSearch(name: String) = getByNameUseCase(name)
        .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            //todo showLoading()
        }
        .doAfterSuccess {
            //todo hideLoading
        }
        .subscribeBy(onSuccess = {
            _charactersList.value = Result.success(it)
        }, onError = { error ->
            _charactersList.value = Result.failure(error)
        }).addTo(disposables)

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}
