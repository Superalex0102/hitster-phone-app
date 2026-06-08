//
//  StubForTeams.swift
//  iosApp
//
//  Created by Ferenc Batorligeti on 2026. 06. 08..
//

import Shared

enum StubForTeams {
	static let teams: [Team] = [
		Team(id: KotlinUuid.companion.random(), name: "Team 1", color: TeamColor.teal),
		Team(id: KotlinUuid.companion.random(), name: "Team 2", color: TeamColor.amber),
		Team(id: KotlinUuid.companion.random(), name: "Team 3", color: TeamColor.crimson),
		Team(id: KotlinUuid.companion.random(), name: "Team 4", color: TeamColor.plum)
	]
}

