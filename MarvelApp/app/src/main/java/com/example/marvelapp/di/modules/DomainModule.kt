package com.example.marvelapp.di.modules

import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.useCases.GetByIdUseCase
import com.example.marvelapp.domain.useCases.GetByNameUseCase
import com.example.marvelapp.domain.useCases.GetCharactersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideGetByNameUseCase(charactersRep: CharactersRep) = GetByNameUseCase(charactersRep)

    @Provides
    fun provideGetByIdUseCase(charactersRep: CharactersRep) = GetByIdUseCase(charactersRep)

    @Provides
    fun provideGetCharactersUseCase(charactersRep: CharactersRep) =
        GetCharactersUseCase(charactersRep)
}
