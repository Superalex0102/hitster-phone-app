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
    
    let winnerTeam: Team = Team(
        id: KotlinUuid.companion.random(),
        name: "Team 1",
        color: TeamColor.teal
    )

    var onHomeClicked: () -> Void
    var onPlayAgainClicked: () -> Void

    var body: some View {
        ZStack {
            MainBackground()

            VStack(spacing: 0) {

                Spacer().frame(height: 20)

                // Logo / Header
                VStack(spacing: 4) {
                    Text(strings.get(R.title))
                        .font(.kdam(size: 20))
                        .foregroundColor(Color(hex: AppColors.shared.WHITE).opacity(0.8))

                    Rectangle()
                        .fill(Color(hex: AppColors.shared.WHITE).opacity(0.6))
                        .frame(width: 120, height: 1)
                }
                .frame(maxWidth: .infinity, alignment: .leading)

                Spacer().frame(height: 30)

                // Title
                Text(strings.get(R.game_summary_title))
                    .font(.kdam(size: 40))
                    .foregroundColor(Color(hex: AppColors.shared.WHITE))

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
                        .foregroundColor(Color(hex: AppColors.shared.WHITE).opacity(0.9))
                }

                Spacer().frame(height: 40)

                // Buttons
                GradientButton(
                    model: GradientButtonModel(
                        title: strings.get(R.home),
                        disabled: false,
                        size: .small,
                        accessibilityId: AccessibilityIds.GameSummaryScreen.shared.HOME_BUTTON,
                        action: onHomeClicked
                    )
                )

                Spacer().frame(height: 16)

                GradientButton(
                    model: GradientButtonModel(
                        title: strings.get(R.play_again),
                        disabled: false,
                        size: .small,
                        accessibilityId: AccessibilityIds.GameSummaryScreen.shared.PLAY_AGAIN_BUTTON,
                        action: onPlayAgainClicked
                    )
                )

                Spacer()
            }
            .padding(.horizontal, 32)
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
        onHomeClicked: { print("Home tapped") },
        onPlayAgainClicked: { print("Play again tapped") }
    )
}

