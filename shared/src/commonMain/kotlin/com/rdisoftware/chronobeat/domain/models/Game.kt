package com.rdisoftware.chronobeat.domain.models

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Game(
    val id: Uuid,
    var collectedCardsByTeam: Map<Uuid, List<String>>
)
