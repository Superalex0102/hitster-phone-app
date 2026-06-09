//
//  MusicDataConverter.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 06. 08..
//

import Shared

struct MusicDataConverter {
    
    static func fromTrackToGameCardItem(trackModel: Track) -> GameCardItemModel {
        GameCardItemModel(
            id: trackModel.id,
            mainArtist: trackModel.mainArtist,
            featArtist: trackModel.featArtists,
            releaseYear: trackModel.releaseYear,
            title: trackModel.title)
    }
}
