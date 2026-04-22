package com.rdisoftware.chronobeat.domain.models

import com.rdisoftware.chronobeat.domain.enums.TeamColor
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Team(
    val id: Uuid,
    val name: String,
    val color: TeamColor
)