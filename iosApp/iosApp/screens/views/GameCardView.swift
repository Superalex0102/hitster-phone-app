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
            Text(model.mainArtist)
                .lineLimit(2, reservesSpace: true)
                .font(.robotoMonoBold(28))
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .multilineTextAlignment(.center)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_ARTIST)
            
            Text(model.featArtist.joined(separator: ", "))
                .font(.robotoMonoLightItalic(12))
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_CONTRIBUTOR)
            
            Text(model.releaseYear)
                .font(.robotoMonoBold(64))
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .frame(alignment: .center)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_YEAR)
            
            Spacer()
            
            Text(model.title)
                .font(.robotoMonoLightItalic(20))
                .foregroundStyle(Color(hex: AppColors.shared.BLACK))
                .lineLimit(2, reservesSpace: true)
                .multilineTextAlignment(.center)
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.GAME_CARD_TITLE)
        }
        .frame(maxWidth: 250, maxHeight: 240)
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
    GameCardView(model: GameCardItemModel(mainArtist: "The Chainsmokers", featArtist: ["Dzsudlo", "Alma"], releaseYear: "2020", title: "ParisParisParisParisParisParis"))
}
