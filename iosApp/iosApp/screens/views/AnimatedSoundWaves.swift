//
//  AnimatedSoundWaves.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 03..
//

import SwiftUI
import Shared

struct AnimatedSoundWaves: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    var isAnimating: Bool
    var barColor: Color = .white
    
    var body: some View {
        HStack(alignment: .center, spacing: 3) {
            ForEach(0..<6, id: \.self) { _ in
                SoundWaveBar(isAnimating: isAnimating, barColor: barColor)
            }
        }
        .frame(height: 30)
        .accessibilityLabel(strings.get(R.content_disc_music_player))
        .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.MUSIC_PLAYER_ICON)
    }
}
 
fileprivate struct SoundWaveBar: View {
    var isAnimating: Bool
    var barColor: Color
    
    @State private var currentHeight: CGFloat = 4.0
    var body: some View {
        Capsule()
            .fill(barColor)
            .frame(width: 3, height: currentHeight)
            .task(id: isAnimating) {
                if isAnimating {
                    let delayMs = UInt64.random(in: 0...300)
                    try? await Task.sleep(nanoseconds: delayMs * 1_000_000)
                    
                    while !Task.isCancelled {
                        let randomMaxHeight = CGFloat.random(in: 20...40)
                        withAnimation(.easeInOut(duration: 0.4)) {
                            currentHeight = randomMaxHeight
                        }
                        try? await Task.sleep(nanoseconds: 400 * 1_000_000)

                        if Task.isCancelled { break }
                        
                        withAnimation(.easeInOut(duration: 0.4)) {
                            currentHeight = 6.0
                        }
                        try? await Task.sleep(nanoseconds: 400 * 1_000_000)
                    }
                } else {
                    withAnimation(.easeOut(duration: 0.3)) {
                        currentHeight = 4.0
                    }
                }
            }
    }
}

#Preview {
    AnimatedSoundWaves(isAnimating: true)
}
