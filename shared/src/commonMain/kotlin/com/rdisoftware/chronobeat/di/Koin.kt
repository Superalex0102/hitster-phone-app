package com.rdisoftware.chronobeat.di

import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.TeamRepositoryImpl
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.CheckSpotifyAuthUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.GetSavedGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.RestartGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.SaveGameProgressUseCase
import com.rdisoftware.chronobeat.domain.usecases.homeScreen.SpotifyAuthenticationUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.GameSummaryViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel
import com.russhwolf.settings.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

val sharedModule = module {
    //Networking
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }
    single { ChronoBeatApi(
        httpClient = get()
    ) }
    single { Settings() }

    //ViewModels
    factory { HomeViewModel(
        getSavedGameUseCase = get(),
        checkSpotifyAuthUseCase = get(),
        spotifyAuthenticationUseCase = get()

    ) }
    factory { GameViewModel(
        playMusicUseCase = get(),
        getSavedGameUseCase = get(),
        saveGameProgressUseCase = get(),
        resetGameUseCase = get(),
        activeGameRepository = get(),
        musicRepository = get(),
    ) }
    factory { TeamSelectionViewModel(
        addTeamUseCase = get(),
        deleteTeamUseCase = get(),
        updateTeamUseCase = get(),
        getTeamsUseCase = get()
    ) }
    factory { GameSummaryViewModel() }

    //UseCases
    factory { PlayMusicUseCase(
        musicRepository = get()
    ) }
    factory { GetSavedGameUseCase(
        activeGameRepository = get()
    ) }
    factory { SaveGameProgressUseCase(
        activeGameRepository = get()
    ) }
    factory { RestartGameUseCase(
        activeGameRepository = get(),
        teamRepository = get()
    ) }
    factory { CheckSpotifyAuthUseCase(
        musicRepository = get()
    ) }

    factory { SpotifyAuthenticationUseCase(
            musicRepository = get()
        )
    }

    factory { AddTeamUseCase(teamRepository = get()) }
    factory { DeleteTeamUseCase(teamRepository = get()) }
    factory { UpdateTeamUseCase(teamRepository = get()) }
    factory { GetTeamsUseCase(teamRepository = get()) }

    //Repositories
    single<TeamRepository> { TeamRepositoryImpl(settings = get()) }
    single<ActiveGameRepository> { ActiveGameRepositoryImpl(settings = get()) }
    single<MusicRepository> {
        MusicRepositoryImpl(
            chronoBeatApi = get(),
            spotifyPlayer = get()
        )
    }
}

fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(sharedModule)
    }
}