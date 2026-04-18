import SwiftUI

struct RootView: View {
    let container: DependencyContainer
    @StateObject var router: AppRouter

    @State private var showRegister = false

    var body: some View {
        Group {
            if router.isAuthenticated {
                HomeView(onLogout: {
                    try? container.authRepository.logout()
                    router.isAuthenticated = false
                })
            } else {
                authFlow
            }
        }
    }

    @ViewBuilder
    private var authFlow: some View {
        let viewModel = container.makeAuthViewModel()

        if showRegister {
            RegisterView(
                viewModel: viewModel,
                onNavigateToLogin: { showRegister = false },
                onNavigateToHome: { router.isAuthenticated = true }
            )
        } else {
            LoginView(
                viewModel: viewModel,
                onNavigateToRegister: { showRegister = true },
                onNavigateToHome: { router.isAuthenticated = true }
            )
        }
    }
}
