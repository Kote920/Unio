import Foundation

@MainActor
final class DependencyContainer {
    let apiClient: AuthAPIClient
    let authRepository: AuthRepository
    let registerUseCase: RegisterUseCase
    let loginUseCase: LoginUseCase

    init() {
        apiClient = AuthAPIClient()
        authRepository = AuthRepository(apiClient: apiClient)
        registerUseCase = RegisterUseCase(repository: authRepository)
        loginUseCase = LoginUseCase(repository: authRepository)
    }

    func makeAuthViewModel() -> AuthViewModel {
        AuthViewModel(
            registerUseCase: registerUseCase,
            loginUseCase: loginUseCase
        )
    }
}
