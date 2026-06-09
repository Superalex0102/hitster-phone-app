//
//  TeamSelectionScreen.swift
//  iosApp
//
//  Created by Ferenc Batorligeti on 2026. 05. 29..
//

import SwiftUI
import Shared

struct TeamSelectionScreen: View {
    
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared

    let teams: [Team]

    var onStartClicked: () -> Void

    var body: some View {
        ZStack {
            // Background gradient
            MainBackground()

            VStack(spacing: 5) {

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
                .padding(.bottom, 25)

                // Title
                Text(strings.get(R.team_selection_title))
                    .font(.kdam(size: 40))
                    .multilineTextAlignment(.center)
                    .foregroundColor(Color(hex: AppColors.shared.WHITE))

                Spacer().frame(height: 30)

                // Input field
                HStack {
                    TeamInputView()
                }
                .padding(.horizontal, 10)

                HStack(spacing: 6) {
                    Image(systemName: "info.circle")
                        .font(.footnote)
                        .foregroundColor(.gray)

                    Text(strings.get(R.ts_info_text))
                        .font(.kdam(size: 15))
                        .foregroundColor(.white.opacity(0.7))
                }
                .padding(.bottom, 25)

                // Team list
                VStack(spacing: 12) {
                    ForEach(Array(teams.enumerated()), id: \.offset) { _, team in
                        TeamRow(
                            title: team.name,
                            color: color(for: team.color)
                        )
                    }
                }
                .font(.kdam(size: 20))
                .padding(.horizontal)

                Spacer()

                // Start button
                GradientButton(
                    model: GradientButtonModel(
                        title: strings.get(R.start),
                        disabled: false,
                        size: .small,
                        accessibilityId: AccessibilityIds.GameSummaryScreen.shared.HOME_BUTTON,
                        action: onStartClicked
                    )
                )

                Spacer()

                BottomText()
            }
            .padding()
        }
    }

    private func color(for teamColor: TeamColor) -> Color {
        switch teamColor {
        case .teal:
            return Color(hex: AppColors.shared.TEAL)
        case .amber:
            return Color(hex: AppColors.shared.AMBER)
        case .crimson:
            return Color(hex: AppColors.shared.RED)
        case .plum:
            return Color(hex: AppColors.shared.PLUM)
        default:
            return Color(hex: AppColors.shared.TEAL)
        }
    }
}

struct TeamRow: View {
    var title: String
    var color: Color

    var body: some View {
        ZStack {
            Text(title)
                .foregroundColor(.white)
                .fontWeight(.medium)
                .frame(maxWidth: .infinity, alignment: .center)

            HStack {
                Spacer()

                HStack(spacing: 12) {
                    Button(action: {}) {
                        Image(systemName: "pencil")
                            .foregroundColor(.white)
                    }

                    Button(action: {}) {
                        Image(systemName: "trash")
                            .foregroundColor(.white)
                    }
                }
            }
        }
        .padding(10)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .fill(
                    LinearGradient(
                        colors: [color.opacity(0.8), color],
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
        )
        .overlay(
            RoundedRectangle(cornerRadius: 15)
                .stroke(Color.white, lineWidth: 2)
        )
    }
}

#Preview("iPhone 15 Pro") {
    TeamSelectionScreen(
        teams: StubForTeams.teams,
        onStartClicked: { print("Start button tapped") }
    )
        .preferredColorScheme(.dark)
}
