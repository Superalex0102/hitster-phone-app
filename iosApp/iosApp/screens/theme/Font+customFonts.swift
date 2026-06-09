//
//  Font+customFonts.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 14..
//

import SwiftUI

extension Font {
    static func kdamThmorProRegular(_ size: CGFloat) -> Font {
        .custom(AppFontName.kdamThmorProRegular.rawValue, size: size)
    }
    
    static func robotoMonoBold(_ size: CGFloat) -> Font {
        .custom(AppFontName.robotoMonoBold.rawValue, size: size)
    }
    
    static func robotoMonoLight(_ size: CGFloat) -> Font {
        .custom(AppFontName.robotoMonoLight.rawValue, size: size)
    }
    
    static func robotoMonoLightItalic(_ size: CGFloat) -> Font {
        .custom(AppFontName.robotoMonoLightItalic.rawValue, size: size)
    }
    
    static func robotoMonoMedium(_ size: CGFloat) -> Font {
        .custom(AppFontName.robotoMonoMedium.rawValue, size: size)
    }
    
    static func robotoMonoRegular(_ size: CGFloat) -> Font {
        .custom(AppFontName.robotoMonoRegular.rawValue, size: size)
    }
}
