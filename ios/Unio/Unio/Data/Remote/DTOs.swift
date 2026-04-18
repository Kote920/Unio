import Foundation

struct RegisterRequest: Encodable {
    let displayName: String?
}

struct UserResponse: Decodable {
    let id: String
    let email: String
    let displayName: String?
    let createdAt: String

    func toDomain() -> User {
        User(id: id, email: email, displayName: displayName)
    }
}
