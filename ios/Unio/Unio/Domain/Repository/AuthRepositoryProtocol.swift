import Foundation

protocol AuthRepositoryProtocol {
    func register(email: String, password: String, displayName: String?) async throws -> User
    func login(email: String, password: String) async throws -> User
    func logout() throws
    var isLoggedIn: Bool { get }
}
