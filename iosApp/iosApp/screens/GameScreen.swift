//
//  GameScreen.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 01..
//

import SwiftUI
import Shared

struct GameScreen: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    
    // MARK: - Fake data for testing
    let cards: [GameCardItemModel] = [
        .init(mainArtist: "The Chainsmokers", featArtist: ["Dzsudlo", "Desh"], releaseYear: "2020", title: "Paris"),
        .init(mainArtist: "The", featArtist: [], releaseYear: "2000", title: "ParisParisParisParisParis"),
        .init(mainArtist: "The Chainsmokers", featArtist: ["Dzsudlo"], releaseYear: "2020", title: "ParisParisParisParisParisParis"),
        .init(mainArtist: "The", featArtist: [], releaseYear: "2020", title: "Paris"),
        .init(mainArtist: "The Chainsmokers", featArtist: [], releaseYear: "2020", title: "Paris")
    ]

    var body: some View {
        ZStack {
            VStack {
                gameHeader
                    .padding(.horizontal, 8)
                
                ScrollView {
                    LazyVStack(spacing: 40) {
                        //Placeholder iteration with fake data
                        ForEach(0...cards.count, id: \.self) { index in
                            
                            guessButton
                            
                            if index < cards.count {
                                GameCardView(model: cards[index])
                            }
                        }
                    }
                }
            }
            
            TimeLineArrowView()
        }
        .background(
            MainBackground()
        )
    }
    
    private var gameHeader: some View {
        HStack {
            teamInformation
            
            AnimatedSoundWaves(isAnimating: true) //Placeholder hardcoded true value
        }
    }
    
    private var teamInformation: some View {
        HStack(spacing: 0) {
            Text("Team 1") //Placeholder hardcoded team name
                .foregroundStyle(Color(hex: AppColors.shared.WHITE))
                .font(.robotoMonoBold(32))
                .padding(.leading, 16)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.TEAM_NAME_TEXT)
                .accessibilityAddTraits(.isStaticText)
            
            Spacer()
            
            Text("5") //Placeholder hardcoded card count
                .foregroundStyle(Color(hex: AppColors.shared.WHITE))
                .font(.robotoMonoRegular(24))
                .padding(.horizontal, 16)
                .padding(.vertical, 10)
                .background {
                    RoundedRectangle(cornerRadius: 15)
                        .fill(Color.clear)
                        .overlay(
                            RoundedRectangle(cornerRadius: 15, style: .continuous)
                                .stroke(.white, lineWidth: 1)
                        )
                }
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.CARD_COUNT_TEXT)
                .accessibilityAddTraits(.isStaticText)
        }
        .background {
            RoundedRectangle(cornerRadius: 15)
                .fill(
                    LinearGradient(
                        gradient: Gradient(colors: [Color(hex: AppColors.shared.TEAL), Color(hex: AppColors.shared.BLACK)]),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .accessibilityAddTraits(.isHeader)
                .accessibilitySortPriority(1)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_HEADER)
                .overlay(
                    RoundedRectangle(cornerRadius: 15, style: .continuous)
                        .stroke(.white, lineWidth: 2)
                )
        }
        .padding(.leading, 8)
    }
    
    private var guessButton: some View {
        Button(
            action: {} //TODO: guess button action
        ) {
            ZStack {
                Circle()
                    .fill(Color(hex: AppColors.shared.GAME_GRAY).opacity(0.6))
                    .frame(width: 48, height: 48)
                
                Circle()
                    .fill(Color(hex: AppColors.shared.GAME_GRAY))
                    .frame(width: 23, height: 23)
            }
        }
        .accessibilityLabel(strings.get(R.content_disc_guess_button))
        .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GUESS_BUTTON)
        .accessibilityAddTraits(.isButton)
        .accessibilityHint(strings.get(R.button_hint))
    }
}

#Preview {
    GameScreen()
}
