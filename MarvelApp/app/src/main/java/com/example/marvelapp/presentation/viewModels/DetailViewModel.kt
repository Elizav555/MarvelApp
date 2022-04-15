package com.example.marvelapp.presentation.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.GetByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val getByIdUseCase: GetByIdUseCase) :
    ViewModel() {
    private var _character: MutableLiveData<Result<Character>> = MutableLiveData()
    val character: LiveData<Result<Character>> = _character

    private var _isLoading: MutableLiveData<Result<Boolean>> = MutableLiveData()
    val isLoading: LiveData<Result<Boolean>> = _isLoading

    private val disposables = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun getCharacter(characterId: Int) = getByIdUseCase(characterId)
        .observeOn(AndroidSchedulers.mainThread()).doOnSubscribe {
            _isLoading.value = Result.success(true)
        }
        .doAfterSuccess {
            _isLoading.value = Result.success(false)
        }
        .subscribeBy(onSuccess = {
            _character.value = Result.success(it)
        }, onError = { error ->
            _character.value = Result.failure(error)
        }).addTo(disposables)
}
