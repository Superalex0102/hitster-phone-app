//
//  GameCardItemModel.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 03..
//

import Foundation

struct GameCardItemModel: Identifiable {
    let id = UUID()
    let mainArtist: String
    let featArtist: [String]
    let releaseYear: String
    let title: String
}
