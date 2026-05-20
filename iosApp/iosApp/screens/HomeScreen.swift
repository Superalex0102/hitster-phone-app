//
//  HomeScreen.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 11..
//

import SwiftUI
import Shared

struct HomeScreen: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    
    var body: some View {
        ZStack {
            VStack {
                settingsButton
                
                mainTitle
                
                Spacer()
                
                BottomText()
            }
                
            gradientButtonStack
        }
        .background(
            MainBackground()
        )
    }
    
    private var settingsButton: some View {
        HStack {
            Spacer()
            
            Button(action: {
                //TODO: Button action
            }) {
                Image(systemName: "gearshape")
                    .foregroundStyle(Color(hex: AppColors.shared.WHITE))
                    .font(.system(size: 34))
            }
            .accessibilityLabel(strings.get(R.settings))
            .accessibilityIdentifier(AccessibilityIds.HomeScreen.shared.SETTINGS_BUTTON)
            .accessibilityAddTraits(.isButton)
            .accessibilityHint(strings.get(R.button_hint))
        }
        .padding(.bottom, 32)
        .padding(.trailing, 16)
    }
    
    private var mainTitle: some View {
        Text(strings.get(R.title))
            .foregroundStyle(Color(hex: AppColors.shared.WHITE))
            .font(AppFont.kdamThmorProRegular(48))
            .padding(.top, 16)
            .accessibilityIdentifier(AccessibilityIds.HomeScreen.shared.CHRONOBEAT_TITLE)
            .accessibilityAddTraits(.isStaticText)
    }
    
    private var gradientButtonStack: some View {
        VStack(spacing: 32) {
            GradientButton(
                model: GradientButtonModel(
                    title: strings.get(R.local_game),
                    disabled: false,
                    size: .large,
                    accessibilityId: AccessibilityIds.HomeScreen.shared.LOCAL_GAME_BUTTON,
                    action: {} //TODO: Button action
                )
            )
            
            GradientButton(
                model: GradientButtonModel(
                    title: strings.get(R.online_game),
                    disabled: true,
                    size: .large,
                    accessibilityId: AccessibilityIds.HomeScreen.shared.ONLINE_GAME_BUTTON,
                    action: {} //TODO: Button action
                )
            )
        }
        .padding(.top, 64)
    }
}

#Preview {
    HomeScreen()
}
