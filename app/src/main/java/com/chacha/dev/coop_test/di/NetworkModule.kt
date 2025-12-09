package com.chacha.dev.coop_test.di

import com.chacha.dev.coop_test.data.remote.api.NetworkApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://test-03.free.beeceptor.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            .create()
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

 /*   @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()*/

    @Provides
    @Singleton
    fun provideNetworkApi(retrofit: Retrofit): NetworkApi =
        retrofit.create(NetworkApi::class.java)
}