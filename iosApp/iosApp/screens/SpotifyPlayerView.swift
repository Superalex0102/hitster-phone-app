import SwiftUI
import Shared

struct SpotifyPlayerView: View {
    let musicRepository: MusicRepositoryImpl
    
    @State private var isPlaying = false
    @State private var hasStarted = false
    
    var body: some View {
        VStack(spacing: 30) {
            Text("ChronoBeat iOS Natív")
                .font(.largeTitle)
                .bold()
            
            Button(action: {
                Task {
                    do {
                        if isPlaying {
                            try await musicRepository.pauseMusic()
                        } else {
                            if !hasStarted {
                                try await musicRepository.playMusic(trackId: "4PTG3Z6ehGkBFwjybzWkR8")
                                hasStarted = true
                            } else {
                                try await musicRepository.resumeMusic()
                            }
                        }
                        isPlaying.toggle()
                    } catch {
                        print("Error: \(error)")
                    }
                }
            }) {
                Text(isPlaying ? "PAUSE" : (hasStarted ? "RESUME" : "PLAY"))
                    .font(.headline)
                    .frame(width: 200, height: 60)
                    .background(isPlaying ? Color.red : Color.green)
                    .foregroundColor(.white)
                    .cornerRadius(15)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(UIColor.systemBackground))
    }
}
