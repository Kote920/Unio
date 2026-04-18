import Foundation
import Combine

@MainActor
final class AuthViewModel: ObservableObject, Sendable {
    @Published var state = AuthState()
    @Published var effect: AuthEffect? = nil

    private let registerUseCase: RegisterUseCase
    private let loginUseCase: LoginUseCase

    init(registerUseCase: RegisterUseCase, loginUseCase: LoginUseCase) {
        self.registerUseCase = registerUseCase
        self.loginUseCase = loginUseCase
    }

    func handleIntent(_ intent: AuthIntent) {
        switch intent {
        case .updateEmail(let email):
            state.email = email
            state.error = nil
        case .updatePassword(let password):
            state.password = password
            state.error = nil
        case .updateDisplayName(let name):
            state.displayName = name
            state.error = nil
        case .register:
            Task { await register() }
        case .login:
            Task { await login() }
        case .clearError:
            state.error = nil
        }
    }

    private func register() async {
        state.isLoading = true
        state.error = nil
        do {
            _ = try await registerUseCase.execute(
                email: state.email,
                password: state.password,
                displayName: state.displayName.isEmpty ? nil : state.displayName
            )
            state.isLoading = false
            effect = .navigateToHome
        } catch {
            state.isLoading = false
            state.error = error.localizedDescription
        }
    }

    private func login() async {
        state.isLoading = true
        state.error = nil
        do {
            _ = try await loginUseCase.execute(
                email: state.email,
                password: state.password
            )
            state.isLoading = false
            effect = .navigateToHome
        } catch {
            state.isLoading = false
            state.error = error.localizedDescription
        }
    }
}
