package com.example.marvelapp.di.modules

import androidx.viewbinding.BuildConfig
import com.example.marvelapp.data.CharactersRepImpl
import com.example.marvelapp.data.MarvelApi
import com.example.marvelapp.data.mappers.CharactersMapper
import com.example.marvelapp.data.utils.MD5
import com.example.marvelapp.di.intercepters.ApiIntercept
import com.example.marvelapp.domain.CharactersRep
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun provideCharactersRepository(
        api: MarvelApi,
        charactersMapper: CharactersMapper,
        mD5: MD5
    ): CharactersRep {
        return CharactersRepImpl(
            api = api,
            charactersMapper = charactersMapper,
            mD5
        )
    }

    @Provides
    fun provideMarvelApi(retrofit: Retrofit): MarvelApi = retrofit
        .create(MarvelApi::class.java)

    @Provides
    fun provideRetrofit(
        okhttp: OkHttpClient,
        converterFactory: GsonConverterFactory,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okhttp)
        .addConverterFactory(converterFactory)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    @Provides
    @ApiIntercept
    fun provideApiKeyInterceptor() = Interceptor { chain ->
        val original = chain.request()
        val newURL = original.url.newBuilder()
            .addQueryParameter(QUERY_API_KEY, API_KEY)
            .build()

        chain.proceed(
            original.newBuilder()
                .url(newURL)
                .build()
        )
    }

    @Provides
    fun provideClient(
        @ApiIntercept apiKeyInterceptor: Interceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
        .also {
                it.addInterceptor(
                    HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)
                )
        }
        .cache(null)
        .build()

    @Provides
    fun provideConvertFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideCharactersMapper(): CharactersMapper = CharactersMapper()

    companion object {
        private const val BASE_URL = "http://gateway.marvel.com"
        private const val API_KEY = com.example.marvelapp.BuildConfig.MARVEL_PUBLIC_KEY
        private const val QUERY_API_KEY = "apikey"
    }
}
