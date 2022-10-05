package com.example.marvelapp.presentation.presenters

import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.GetByNameUseCase
import com.example.marvelapp.domain.useCases.GetCharactersUseCase
import com.example.marvelapp.domain.useCases.utils.RxJavaRule
import com.example.marvelapp.presentation.views.`CharactersListView$$State`
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
internal class ListPresenterTest {
    @get:Rule
    val rxJavaRule = RxJavaRule()

    @MockK
    lateinit var getByNameUseCase: GetByNameUseCase

    @MockK
    lateinit var getCharactersUseCase: GetCharactersUseCase

    @MockK
    lateinit var router: Router

    @MockK(relaxUnitFun = true)
    lateinit var viewState: `CharactersListView$$State`

    private lateinit var presenter: ListPresenter

    @BeforeEach
    fun setUp() {
        presenter = spyk(
            ListPresenter(getByNameUseCase, getCharactersUseCase, router, rxJavaRule.scheduler)
        )
        presenter.setViewState(viewState)
    }

    @Test
    fun onCharactersSearch() {
        val expectedSearch = "Spider"
        val expectedCharacterId = 1012295
        val expectedCharacterName = "Spider-Man (Noir)"
        val mockCharacter = mockk<Character>() {
            every { id } returns expectedCharacterId
            every { name } returns expectedCharacterName
        }
        every {
            getByNameUseCase(expectedSearch)
        } returns Single.just(listOf(mockCharacter))

        // act
        presenter.onCharactersSearch(expectedSearch)

        // assert
        verifyOrder {
            viewState.showLoading()
            viewState.hideLoading()
        }
        verify {
            viewState.updateList(listOf(mockCharacter))
        }
    }

    @Test
    fun getCharacters() {
        val expectedCharacterId = 1012295
        val expectedCharacterName = "Spider-Man (Noir)"
        val mockCharacter = mockk<Character>() {
            every { id } returns expectedCharacterId
            every { name } returns expectedCharacterName
        }
        every {
            getCharactersUseCase()
        } returns Single.just(listOf(mockCharacter))
        // act
        presenter.getCharacters()

        // assert
        verifyOrder {
            viewState.showLoading()
            viewState.hideLoading()
        }
        verify {
            viewState.updateList(listOf(mockCharacter))
        }
    }

    @Test
    fun onCharactersSearchError() {
        // arrange
        val mockError = mockk<Throwable>()
        val expectedSearch = "Spider"
        every {
            getByNameUseCase(expectedSearch)
        } returns Single.error(mockError)

        // act
        presenter.onCharactersSearch(expectedSearch)

        // assert
        verifyOrder {
            viewState.showLoading()
        }
        verify(inverse = true) {
            viewState.updateList(any())
        }
        verify {
            viewState.showError(mockError)
        }
    }
}