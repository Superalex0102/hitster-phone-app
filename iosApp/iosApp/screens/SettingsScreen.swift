//
//  SettingsScreen.swift
//  iosApp
//
//  Created by Ferenc Batorligeti on 2026. 06. 08..
//

import SwiftUI
import Shared

struct SettingsScreen: View {

    @Environment(\.dismiss) private var dismiss

    @StateObject private var strings = StringProvider.shared
    let R = Res.string.shared

    @State private var darkMode = true
    @State private var notifications = false
    @State private var selectedLanguage: String

    init() {
        let strings = StringProvider.shared
        let r = Res.string.shared
        _selectedLanguage = State(initialValue: strings.get(r.hungarian))
    }

    private var languages: [String] {
        [
            strings.get(R.hungarian),
            strings.get(R.english),
            strings.get(R.german)
        ]
    }

    var body: some View {

        ZStack {

            Color.black
                .ignoresSafeArea()

            VStack(spacing: 30) {

                Spacer()

                Text(strings.get(R.settings))
                    .font(.kdam(size: 48))
                    .foregroundColor(.white)

                VStack(spacing: 0) {

                    settingToggleRow(
                        title: strings.get(R.dark_mode),
                        isOn: $darkMode
                    )
                    .padding(.horizontal, 32)

                    Divider()
                        .background(Color(hex: 0xFF474747))
                        .padding(.horizontal)


                    settingToggleRow(
                        title: strings.get(R.notifications),
                        isOn: $notifications
                    )
                    .padding(.horizontal, 32)

                    Divider()
                        .background(Color(hex: 0xFF474747))
                        .padding(.horizontal)

                }

                VStack(spacing: 20) {
                    Text(strings.get(R.supported_languages))
                        .font(AppFont.robotoMonoBold(32))
                        .foregroundColor(.white)
                        .multilineTextAlignment(.center)

                    Divider()
                        .background(Color(hex: 0xFF474747))
                        .padding(.horizontal)

                    VStack(spacing: 0) {

                        ForEach(languages, id: \.self) { language in

                            Button {

                                selectedLanguage = language

                            } label: {

                                ZStack {

                                    Text(language)
                                        .font(AppFont.robotoMonoBold(24))
                                        .foregroundColor(.white)
                                        .frame(maxWidth: .infinity, alignment: .center)

                                    HStack {
                                        Spacer()
                                        Image(systemName: "checkmark")
                                            .foregroundColor(.white)
                                            .opacity(selectedLanguage == language ? 1 : 0)
                                    }
                                    .frame(width: 250)
                                }
                                .frame(maxWidth: .infinity)
                                .padding(.vertical, 16)
                            }

                            Divider()
                                .background(Color(hex: 0xFF474747))
                                .padding(.horizontal, 40)
                        }
                    }
                }

                Spacer()

                Button {

                    dismiss()

                } label: {

                    Text(strings.get(R.save))
                        .font(AppFont.robotoMonoBold(32))
                        .foregroundColor(.white)
                        .frame(width: 220, height: 64)
                        .background(
                            LinearGradient(
                                colors: [
                                    Color.gray.opacity(0.5),
                                    Color.black
                                ],
                                startPoint: .leading,
                                endPoint: .trailing
                            )
                        )
                        .overlay(
                            Capsule()
                                .stroke(Color.white, lineWidth: 2)
                        )
                        .clipShape(Capsule())
                }

                Spacer()

                BottomText()

                Spacer()

            }
            .padding(.vertical, 30)
        }
        .navigationBarBackButtonHidden(true)
    }

    @ViewBuilder
    private func settingToggleRow(
        title: String,
        isOn: Binding<Bool>
    ) -> some View {

        HStack(spacing: 0) {

            Text(title)
                .font(AppFont.robotoMonoBold(32))
                .foregroundColor(.white)

            Spacer()

            Button {
                withAnimation(.easeInOut(duration: 0.2)) {
                    isOn.wrappedValue.toggle()
                }
            } label: {
                ZStack(alignment: isOn.wrappedValue ? .trailing : .leading) {
                    Capsule()
                        .fill(isOn.wrappedValue ? Color.white : Color.white.opacity(0.12))
                        .frame(width: 72, height: 40)
                        .overlay(
                            Capsule()
                                .stroke(Color.white.opacity(0.7), lineWidth: 1)
                        )

                    ZStack {
                        Circle()
                            .fill(isOn.wrappedValue ? Color.black : Color.gray)
                            .frame(width: 30, height: 30)

                        Image(systemName: isOn.wrappedValue ? "checkmark" : "xmark")
                            .font(.system(size: 14, weight: .bold))
                            .foregroundColor(isOn.wrappedValue ? .white : .black)
                    }
                    .padding(.horizontal, 4)
                }
            }
            .buttonStyle(.plain)
            .accessibilityLabel(title)
            .accessibilityValue(isOn.wrappedValue ? "On" : "Off")
        }
        .frame(maxWidth: .infinity)
        .padding(.vertical, 20)
    }
}

#Preview {
    SettingsScreen()
}
