package com.rdisoftware.chronobeat.presentation.preview

import com.rdisoftware.chronobeat.presentation.model.HeaderModel

object MockHeaderData {
    val data = listOf(
        HeaderModel(
            teamName = "Team One",
            cardCount = "3",
            isMusicOn = true
        ),
        HeaderModel(
            teamName = "Team Two",
            cardCount = "7",
            isMusicOn = false
        ),
        HeaderModel(
            teamName = "Team Three",
            cardCount = "9",
            isMusicOn = true
        ),
        HeaderModel(
            teamName = "Team Four",
            cardCount = "0",
            isMusicOn = false
        )
    )
}