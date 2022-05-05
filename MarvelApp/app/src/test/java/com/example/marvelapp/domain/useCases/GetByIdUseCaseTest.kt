package com.example.marvelapp.domain.useCases

import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.entities.Character
import com.example.marvelapp.domain.useCases.utils.RxJavaRule
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith


@ExtendWith(MockKExtension::class)
internal class GetByIdUseCaseTest {

    @MockK
    lateinit var charactersRep: CharactersRep
    lateinit var getByIdUseCase: GetByIdUseCase

    @get:Rule
    val rxJavaRule = RxJavaRule()

    @BeforeEach
    fun setUp() {
        getByIdUseCase = GetByIdUseCase(charactersRep)
    }

    @Test
    @DisplayName("Test if we get right character when we pass its id")
    fun invokeTest() {
        // arrange
        val expectedValue = 1012295
        val expectedCharacterId = 1012295
        val expectedCharacterName = "Spider-Man (Noir)"
        val mockCharacter = mockk<Character>() {
            every { id } returns expectedCharacterId
            every { name } returns expectedCharacterName
        }
        every {
            charactersRep.getCharacterById(expectedValue)
        } returns Single.just(mockCharacter)

        // act
        val result = getByIdUseCase(expectedValue)

        // assert
        assertEquals(
            expectedCharacterId,
            result.blockingGet().id
        )
        assertEquals(
            expectedCharacterName,
            result.blockingGet().name
        )
    }
}