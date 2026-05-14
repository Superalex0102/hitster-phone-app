package com.rdisoftware.chronobeat.di

import com.rdisoftware.chronobeat.domain.player.SpotifyPlayerController
import com.rdisoftware.chronobeat.domain.repositories.MusicRepository
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

fun initKoinIOS(spotifyController: SpotifyPlayerController) {
    initKoin {
        modules( module {
            single<SpotifyPlayerController> { spotifyController}
        })
    }
}

fun getMusicRepository(): MusicRepository {
    return KoinPlatform.getKoin().get()
}