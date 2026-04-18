import SwiftUI

struct HomeView: View {
    var onLogout: () -> Void

    var body: some View {
        VStack(spacing: 24) {
            Spacer()

            Text("Welcome to Unio")
                .font(.title)
                .fontWeight(.bold)

            Button("Log Out", role: .destructive) {
                onLogout()
            }
            .buttonStyle(.bordered)

            Spacer()
        }
        .padding(24)
    }
}
