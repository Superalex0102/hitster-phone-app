//
//  GradientButton.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 18..
//

import SwiftUI
import Shared

enum ButtonSize {
    case small
    case large
    
    var width: CGFloat {
        switch self {
        case .small:
            return 0.45
        case .large:
            return 0.7
        }
    }
    
    var height: CGFloat {
        switch self {
        case .small:
            return 0.09
        case .large:
            return 0.1
        }
    }
    
    var fontSize: CGFloat {
        switch self {
        case .small:
            return 24
        case .large:
            return 32
        }
    }
}

struct GradientButton: View {
    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared
    let model: GradientButtonModel
    
    var body: some View {
        Button(action: {
            model.action()
        }) {
            Text(model.title)
                .foregroundStyle(Color(hex: AppColors.shared.WHITE))
                .font(AppFont.robotoMonoBold(model.size.fontSize))
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(
                    LinearGradient(
                        gradient: Gradient(colors: [Color(hex: AppColors.shared.DARK_GRAY), Color(hex: AppColors.shared.BLACK)]),
                        startPoint: .leading,
                        endPoint: .trailing
                    )
                )
                .clipShape(Capsule())
                .overlay(
                    Capsule()
                        .stroke(Color(hex: AppColors.shared.WHITE), lineWidth: 2)
                )
        }
        .disabled(model.disabled)
        .opacity(model.disabled ? 0.4 : 1)
        .containerRelativeFrame([.horizontal, .vertical]) { length, axis in
            axis == .horizontal ? length * model.size.width : length * model.size.height
        }
        .accessibilityIdentifier(model.accessibilityId)
        .accessibilityAddTraits(.isButton)
        .accessibilityHint(strings.get(R.button_hint))
    }
}

#Preview {
    GradientButton(model: GradientButtonModel(title: "Local", disabled: false, size: .large, accessibilityId: "", action: {}))
}
