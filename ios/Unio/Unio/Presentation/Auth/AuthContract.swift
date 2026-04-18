import Foundation

struct AuthState: Equatable {
    var email: String = ""
    var password: String = ""
    var displayName: String = ""
    var isLoading: Bool = false
    var error: String? = nil
}

enum AuthIntent {
    case updateEmail(String)
    case updatePassword(String)
    case updateDisplayName(String)
    case register
    case login
    case clearError
}

enum AuthEffect: Equatable {
    case navigateToHome
    case showError(String)
}
