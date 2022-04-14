package com.example.marvelapp.di.modules

import com.example.marvelapp.domain.CharactersRep
import com.example.marvelapp.domain.useCases.GetByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideGetByNameUseCase(charactersRep: CharactersRep) = GetByNameUseCase(charactersRep)
}
