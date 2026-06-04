//
//  TimeLineArrowView.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 03..
//

import SwiftUI
import Shared

struct TimeLineArrowView: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    
    var body: some View {
        ZStack(alignment: .leading) {
            VStack(spacing: 0) {
                Spacer(minLength: 100)
                Group {
                    Rectangle()
                        .fill(Color(hex: AppColors.shared.WHITE).opacity(0.7))
                        .frame(width: 2)
                    
                    Image(systemName: "chevron.down")
                        .foregroundStyle(Color(hex: AppColors.shared.WHITE).opacity(0.7))
                        .padding(.top, -8)
                }
                .accessibilityLabel(strings.get(R.content_disc_arrow))
                .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.TIME_LINE_ARROW)
            }
            .frame(maxWidth: .infinity, alignment: .bottomLeading)
            .padding(.leading, 8)
            
            VStack(spacing: 0) {
                Spacer()
                    .frame(maxHeight: 100)
                
                Text(strings.get(R.arrow_oldest_text))
                    .foregroundStyle(Color(hex: AppColors.shared.WHITE).opacity(0.7))
                    .font(.robotoMonoBold(14))
                    .rotationEffect(.degrees(90))
                    .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.ARROW_OLDEST_TEXT)
                    .accessibilityAddTraits(.isStaticText)
                
                Spacer()
                
                Text(strings.get(R.arrow_latest_text))
                    .foregroundStyle(Color(hex: AppColors.shared.WHITE).opacity(0.7))
                    .font(.robotoMonoBold(14))
                    .rotationEffect(.degrees(90))
                    .accessibilityIdentifier(AccessibilityIds.GameScreen.shared.ARROW_LATEST_TEXT)
                    .accessibilityAddTraits(.isStaticText)
            }
            .frame(maxWidth: .infinity, alignment: .bottomLeading)
            .padding(.leading, 4)
            .padding(.top, 16)
            .padding(.bottom, 30)
        }
    }
}

#Preview {
    TimeLineArrowView()
}
