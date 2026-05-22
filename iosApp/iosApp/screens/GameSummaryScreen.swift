//
//  GameSummaryScreen.swift
//  iosApp
//
//  Created by Ferenc Batorligeti on 2026. 05. 21..
//

import SwiftUI
import Shared

struct GameSummaryScreen: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    let winnerTeam: Team

    var onHomeClicked: () -> Void
    var onPlayAgainClicked: () -> Void

    var body: some View {
        ZStack {
            // Fullscreen dark background
            LinearGradient(
                colors: [
                    Color.black,
                    Color(white: 0.08),
                    Color.black
                ],
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .ignoresSafeArea()

            VStack(spacing: 0) {

                Spacer().frame(height: 20)

                // Logo / Header
                VStack(spacing: 4) {
                    Text(strings.get(R.title))
                        .font(.kdam(size: 20))
                        .foregroundColor(.white.opacity(0.8))

                    Rectangle()
                        .fill(Color.white.opacity(0.6))
                        .frame(width: 120, height: 1)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Spacer().frame(height: 30)

                // Title
                Text(strings.get(R.game_summary_title))
                    .font(.kdam(size: 40))
                    .foregroundColor(.white)

                Spacer().frame(height: 30)

                // Trophy
                Image("trophy")
                    .resizable()
                    .scaledToFit()
                    .frame(height: 200)

                Spacer().frame(height: 24)

                // Winner text
                VStack(spacing: 6) {
                    Text(winnerTeam.name)
                        .font(.kdam(size: 30))
                        .foregroundColor(.white)

                    Text(strings.get(R.won_the_game))
                        .font(.kdam(size: 30))
                        .foregroundColor(.white.opacity(0.9))
                }

                Spacer().frame(height: 40)

                // Buttons
                OutlineButton(title: strings.get(R.home), action: onHomeClicked)

                Spacer().frame(height: 16)

                OutlineButton(title: strings.get(R.play_again), action: onPlayAgainClicked)

                Spacer()
            }
            .padding(.horizontal, 32)
        }
    }
}

struct OutlineButton: View {
    let title: String
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            Text(title)
                .font(.kdam(size: 18))
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .frame(height: 52)
                .background(
                    LinearGradient(
                        colors: [
                            Color.white.opacity(0.15),
                            Color.black
                        ],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .overlay(
                    RoundedRectangle(cornerRadius: 28)
                        .stroke(Color.white.opacity(0.8), lineWidth: 1.5)
                )
                .cornerRadius(28)
        }
    }
}

extension Font {
    static func kdam(size: CGFloat) -> Font {
        .custom("KdamThmorPro-Regular", size: size)
    }
}

#Preview {
    GameSummaryScreen(
        winnerTeam: Team(
            id: KotlinUuid.companion.random(),
            name: "Team 1",
            color: TeamColor.teal
        ),
        onHomeClicked: { print("Home tapped") },
        onPlayAgainClicked: { print("Play again tapped") }
    )
}

