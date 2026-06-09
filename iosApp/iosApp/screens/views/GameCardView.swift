//
//  GameCardView.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 03..
//

import SwiftUI
import Shared

struct GameCardView: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    let model: GameCardItemModel
    
    var body: some View {
        VStack(alignment: .center) {
            Spacer()
            Text(model.mainArtist)
                .lineLimit(2, reservesSpace: false)
                .font(.robotoMonoBold(28))
                .padding(0)
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .multilineTextAlignment(.center)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_ARTIST)
            if !model.featArtist.isEmpty {
                Text(model.featArtist.joined(separator: ", "))
                    .lineLimit(1)
                    .font(.robotoMonoLightItalic(12))
                    .padding(0)
                    .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                    .multilineTextAlignment(.center)
                    .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_CONTRIBUTOR)
            }
            
            Spacer()
            
            Text(String(model.releaseYear))
                .font(.robotoMonoBold(64))
                .padding(-10)
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_YEAR)
            
            Spacer()
            
            Text(model.title)
                .lineLimit(2, reservesSpace: false)
                .font(.robotoMonoLightItalic(20))
                .padding(0)
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .multilineTextAlignment(.center)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_TITLE)
            
            Spacer()
        }
        .frame(width: 250, height: 240)
        .padding(.all, 8)
        .background(Color(hex: AppColors.shared.GAME_GRAY))
        .clipShape(
            RoundedRectangle(cornerRadius: 20)
        )
        .overlay(
            RoundedRectangle(cornerRadius: 20)
                .stroke(Color(hex: AppColors.shared.TEAL), lineWidth: 3)
                .accessibilityLabel(strings.get(R.content_disc_game_card))
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD)
        )
    }
}

#Preview {
    GameCardView(
        model: GameCardItemModel(
            id: "1",
            mainArtist: "The Chainsmokers",
            featArtist: ["Dzsudlo", "Alma"],
            releaseYear: 2020,
            title: "ParisParisParisParisParisParis"
        )
    )
}
