//
//  StringProvider.swift
//  iosApp
//
//  Created by Balazs Tibor Hollery on 2026. 05. 20..
//

import SwiftUI
import Shared
 
class StringProvider: ObservableObject {
    
    static let shared = StringProvider()
    @Published private var cache: [Int: String] = [:]
    
    private init() {}
    
    func get(_ resource: LibraryStringResource) -> String {
        let key = resource.hash
        if let cachedString = cache[key] {
            return cachedString
        }
        Task {
            if let realText = try? await ResourceBridge.shared.getStringAsync(resource: resource) {
                await MainActor.run {
                    self.cache[key] = realText
                }
            }
        }
        return ""
    }
}
