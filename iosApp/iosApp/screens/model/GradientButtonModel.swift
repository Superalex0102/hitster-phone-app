//
//  GradientButtonModel.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 18..
//

struct GradientButtonModel {
    let title: String
    let disabled: Bool
    let size: ButtonSize
    let accessibilityId: String
    let action: () -> Void
}
