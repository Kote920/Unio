import Foundation

enum AuthError: LocalizedError {
    case invalidEmail
    case passwordTooShort
    case networkError(String)
    case serverError(String)
    case unknown

    var errorDescription: String? {
        switch self {
        case .invalidEmail:
            return "Please enter a valid email address"
        case .passwordTooShort:
            return "Password must be at least 6 characters"
        case .networkError(let message):
            return message
        case .serverError(let message):
            return message
        case .unknown:
            return "An unexpected error occurred"
        }
    }
}
