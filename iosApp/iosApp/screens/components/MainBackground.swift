//
//  MainBackground.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 11..
//

import SwiftUI
import Shared

struct MainBackground: View {
    var body: some View {
        LinearGradient(
            gradient: Gradient(colors: [
                Color(hex: AppColors.shared.BLACK),
                Color(hex: AppColors.shared.DARK_GRAY),
                Color(hex: AppColors.shared.BLACK)
            ]),
            startPoint: .top,
            endPoint: .bottom
        )
        .ignoresSafeArea()
    }
}

#Preview {
    MainBackground()
}
