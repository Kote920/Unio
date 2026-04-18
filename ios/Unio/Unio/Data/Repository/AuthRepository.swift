import Foundation
import FirebaseAuth

final class AuthRepository: AuthRepositoryProtocol {
    private let apiClient: AuthAPIClient

    init(apiClient: AuthAPIClient) {
        self.apiClient = apiClient
    }

    var isLoggedIn: Bool {
        Auth.auth().currentUser != nil
    }

    func register(email: String, password: String, displayName: String?) async throws -> User {
        let result = try await Auth.auth().createUser(withEmail: email, password: password)
        let token = try await result.user.getIDToken()
        let response = try await apiClient.register(token: token, displayName: displayName)
        return response.toDomain()
    }

    func login(email: String, password: String) async throws -> User {
        let result = try await Auth.auth().signIn(withEmail: email, password: password)
        let token = try await result.user.getIDToken()
        let response = try await apiClient.me(token: token)
        return response.toDomain()
    }

    func logout() throws {
        try Auth.auth().signOut()
    }
}
