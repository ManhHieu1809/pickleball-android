package com.example.pickleball.di

import com.example.pickleball.data.remote.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module 
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO: Change this to your actual server URL
    private const val BASE_URL = "http://192.168.32.103:8080/api/"

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    // ─── API Service Providers ───────────────────────────────────────────────

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService =
        retrofit.create(AuthApiService::class.java)

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService =
        retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideVenueApiService(retrofit: Retrofit): VenueApiService =
        retrofit.create(VenueApiService::class.java)

    @Provides
    @Singleton
    fun provideCourtApiService(retrofit: Retrofit): CourtApiService =
        retrofit.create(CourtApiService::class.java)

    @Provides
    @Singleton
    fun provideTimeSlotApiService(retrofit: Retrofit): TimeSlotApiService =
        retrofit.create(TimeSlotApiService::class.java)

    @Provides
    @Singleton
    fun provideBookingApiService(retrofit: Retrofit): BookingApiService =
        retrofit.create(BookingApiService::class.java)

    @Provides
    @Singleton
    fun providePlayerApiService(retrofit: Retrofit): PlayerApiService =
        retrofit.create(PlayerApiService::class.java)

    @Provides
    @Singleton
    fun provideLeaderboardApiService(retrofit: Retrofit): LeaderboardApiService =
        retrofit.create(LeaderboardApiService::class.java)

    @Provides
    @Singleton
    fun provideRefereeApiService(retrofit: Retrofit): RefereeApiService =
        retrofit.create(RefereeApiService::class.java)

    @Provides
    @Singleton
    fun provideWalletApiService(retrofit: Retrofit): WalletApiService =
        retrofit.create(WalletApiService::class.java)
}
