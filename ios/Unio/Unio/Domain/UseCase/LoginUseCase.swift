import Foundation

final class LoginUseCase {
    private let repository: AuthRepositoryProtocol

    init(repository: AuthRepositoryProtocol) {
        self.repository = repository
    }

    func execute(email: String, password: String) async throws -> User {
        guard !email.trimmingCharacters(in: .whitespaces).isEmpty else {
            throw AuthError.invalidEmail
        }
        guard !password.isEmpty else {
            throw AuthError.passwordTooShort
        }
        return try await repository.login(
            email: email.trimmingCharacters(in: .whitespaces),
            password: password
        )
    }
}
