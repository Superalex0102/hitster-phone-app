package com.rdisoftware.chronobeat.di

import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.TeamRepositoryImpl
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.TeamSelectionViewModel
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

    //ViewModels
    factory { HomeViewModel() }
    factory { GameViewModel(
        playMusicUseCase = get()
    ) }
    factory { TeamSelectionViewModel(
        addTeamUseCase = get(),
        deleteTeamUseCase = get(),
        updateTeamUseCase = get(),
        getTeamsUseCase = get()
    ) }

    //UseCases
    factory { PlayMusicUseCase(
        musicRepository = get()
    ) }

    factory { AddTeamUseCase(teamRepository = get()) }
    factory { DeleteTeamUseCase(teamRepository = get()) }
    factory { UpdateTeamUseCase(teamRepository = get()) }
    factory { GetTeamsUseCase(teamRepository = get()) }

    //Repositories
    single<TeamRepository> { TeamRepositoryImpl() }
    single<ActiveGameRepository> { ActiveGameRepositoryImpl() }
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