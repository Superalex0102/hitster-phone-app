//
//  BottomText.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 19..
//

import SwiftUI
import Shared

struct BottomText: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    
    var body: some View {
        Group {
            Text(strings.get(R.powered_by))
                .foregroundStyle(Color(hex: AppColors.shared.BOTTOM_GRAY))
                .font(AppFont.robotoMonoLight(16))
            +
            Text(strings.get(R.bottom_app_name))
                .foregroundStyle(Color(hex: AppColors.shared.BOTTOM_GRAY))
                .font(AppFont.robotoMonoBold(16))
        }
        .accessibilityIdentifier(AccessibilityIds.Common.shared.BOTTOM_TEXT)
        .accessibilityAddTraits(.isStaticText)
    }
}

#Preview {
    BottomText()
}
