package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.GetByIdUseCase
import com.example.marvelapp.domain.useCases.utils.RxJavaRule
import com.example.marvelapp.presentation.views.`DetailView$$State`
import com.github.terrakok.cicerone.Router
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class DetailPresenterTest {
    @get:Rule
    val rxJavaRule = RxJavaRule()

    @MockK
    lateinit var getByIdUseCase: GetByIdUseCase

    @MockK
    lateinit var router: Router

    @MockK(relaxUnitFun = true)
    lateinit var viewState: `DetailView$$State`

    private lateinit var presenter: DetailPresenter

    @BeforeEach
    fun setUp() {
        presenter = spyk(
            DetailPresenter(getByIdUseCase, router, rxJavaRule.scheduler)
        )
        presenter.setViewState(viewState)
    }

    @Test
    fun getCharacter() {
        val expectedCharacterId = 1012295
        val expectedCharacterName = "Spider-Man (Noir)"
        val mockCharacter = mockk<Character>() {
            every { id } returns expectedCharacterId
            every { name } returns expectedCharacterName
        }
        every {
            getByIdUseCase(expectedCharacterId)
        } returns Single.just(mockCharacter)

        // act
        presenter.getCharacter(expectedCharacterId)

        // assert
        verifyOrder {
            viewState.showLoading()
            viewState.hideLoading()
        }
        verify {
            viewState.showCharacter(mockCharacter)
        }
    }

    @Test
    fun getCharacterError() {
        // arrange
        val expectedCharacterId = 1012295
        val mockError = mockk<Throwable>()
        every {
            getByIdUseCase(expectedCharacterId)
        } returns Single.error(mockError)
        // act
        presenter.getCharacter(expectedCharacterId)

        // assert
        verifyOrder {
            viewState.showLoading()
        }
        verify(inverse = true) {
            viewState.showCharacter(any())
        }
        verify {
            viewState.showError(mockError)
        }
    }
}