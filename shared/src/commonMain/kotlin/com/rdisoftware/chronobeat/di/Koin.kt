package com.rdisoftware.chronobeat.di

import com.rdisoftware.chronobeat.data.remote.api.ChronoBeatApi
import com.rdisoftware.chronobeat.data.repositories.ActiveGameRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.MusicRepositoryImpl
import com.rdisoftware.chronobeat.data.repositories.TeamRepositoryImpl
import com.rdisoftware.chronobeat.domain.repositories.ActiveGameRepository
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import com.rdisoftware.chronobeat.domain.repositories.TeamRepository
import com.rdisoftware.chronobeat.domain.usecases.game.AdvanceTurnUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.CheckGuessPositionUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.ClearGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetChronobeatPlaylistsUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.GetGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.GetWinnerTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.GetPlayableTrackUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.PlayMusicUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.ProcessCorrectGuessUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SaveGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.SetupInitialGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.CheckSpotifyAuthUseCase
import com.rdisoftware.chronobeat.domain.usecases.game.RestartGameUseCase
import com.rdisoftware.chronobeat.domain.usecases.music.SpotifyAuthenticationUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.AddTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.DeleteTeamUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.GetTeamsUseCase
import com.rdisoftware.chronobeat.domain.usecases.team.UpdateTeamUseCase
import com.rdisoftware.chronobeat.presentation.enums.LeaderBoardMode
import com.rdisoftware.chronobeat.presentation.viewmodels.GameSummaryViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.GameViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.HomeViewModel
import com.rdisoftware.chronobeat.presentation.viewmodels.LeaderBoardViewModel
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
        getGameUseCase = get(),
        checkSpotifyAuthUseCase = get(),
        spotifyAuthenticationUseCase = get(),
        restartGameUseCase = get()
    ) }
    factory { GameViewModel(
        getPlayableTrackUseCase = get(),
        setupInitialGameUseCase = get(),
        checkGuessPositionUseCase = get(),
        processCorrectGuessUseCase = get(),
        advanceTurnUseCase = get(),
        playMusicUseCase = get(),
        getGameUseCase = get(),
        saveGameUseCase = get(),
        getChronobeatPlaylistsUseCase = get(),
        getTeamsUseCase = get(),
    ) }
    factory { TeamSelectionViewModel(
        addTeamUseCase = get(),
        deleteTeamUseCase = get(),
        updateTeamUseCase = get(),
        getTeamsUseCase = get(),
        clearGameUseCase = get()
    ) }
    factory { GameSummaryViewModel(
        getWinnerTeamUseCase = get()
    ) }
    factory { LeaderBoardViewModel(getGameUseCase = get()) }

    //UseCases
    factory { PlayMusicUseCase(musicRepository = get()) }
    factory { GetPlayableTrackUseCase(musicRepository = get()) }
    factory { SetupInitialGameUseCase(
        getPlayableTrackUseCase = get(),
        activeGameRepository = get()
    ) }
    factory { CheckGuessPositionUseCase() }
    factory { ProcessCorrectGuessUseCase() }
    factory { AdvanceTurnUseCase() }
    factory { AddTeamUseCase(teamRepository = get()) }
    factory { DeleteTeamUseCase(teamRepository = get()) }
    factory { UpdateTeamUseCase(teamRepository = get()) }
    factory { GetTeamsUseCase(teamRepository = get()) }
    factory { GetGameUseCase(
        activeGameRepository = get(),
        musicRepository = get(),
        getTeamsUseCase = get(),
    ) }
    factory { SaveGameUseCase(activeGameRepository = get()) }
    factory { GetChronobeatPlaylistsUseCase(musicRepository = get()) }
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
    factory { GetWinnerTeamUseCase(
        getGameUseCase = get()
    ) }
    factory { ClearGameUseCase(
        activeGameRepository = get()
    ) }

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