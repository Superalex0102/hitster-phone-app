//
//  TeamInputView.swift
//  iosApp
//
//  Created by Ferenc Batorligeti on 2026. 06. 04..
//

import SwiftUI
import Shared

struct TeamInputView: View {
    @State private var teamName: String = ""

    var body: some View {
        HStack(spacing: 0) {
            TextField(
                "",
                text: $teamName,
                prompt: Text("Enter Team Name")
                    .foregroundColor(.white.opacity(0.6))
            )
            .font(.kdam(size: 20))
            .multilineTextAlignment(.center)
            .foregroundColor(.white)
            .padding()
                .background(
                                LinearGradient(
                                    gradient: Gradient(colors: [Color(hex: AppColors.shared.DARK_GRAY), Color(hex: AppColors.shared.BLACK)]),
                                    startPoint: .leading,
                                    endPoint: .trailing
                                )
                            )
                            .clipShape(Capsule())
            Button {
                // handle add action
                print("Add team: \(teamName)")
            } label: {
                Image(systemName: "plus")
                    .foregroundColor(.white)
                    .frame(width: 55, height: 55)
                    .background(Color.black)
                    .clipShape(Circle())
                    .overlay(
                        Circle().stroke(Color.white, lineWidth: 2)
                    )
            }
            .padding(.leading, -50)
        }
        .overlay(
            RoundedRectangle(cornerRadius: 40)
                .stroke(Color.white, lineWidth: 2)
        )
    }
}

#Preview {
    ZStack {
        Color.gray.ignoresSafeArea()

        TeamInputView()
    }
}
