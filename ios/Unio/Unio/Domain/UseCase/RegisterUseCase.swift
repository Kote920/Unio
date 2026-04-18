import Foundation

final class RegisterUseCase {
    private let repository: AuthRepositoryProtocol

    init(repository: AuthRepositoryProtocol) {
        self.repository = repository
    }

    func execute(email: String, password: String, displayName: String?) async throws -> User {
        guard !email.trimmingCharacters(in: .whitespaces).isEmpty else {
            throw AuthError.invalidEmail
        }
        guard password.count >= 6 else {
            throw AuthError.passwordTooShort
        }
        return try await repository.register(
            email: email.trimmingCharacters(in: .whitespaces),
            password: password,
            displayName: displayName
        )
    }
}
