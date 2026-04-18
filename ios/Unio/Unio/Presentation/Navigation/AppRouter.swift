import Foundation
import Combine

@MainActor
final class AppRouter: ObservableObject, Sendable {
    @Published var isAuthenticated: Bool

    init(isAuthenticated: Bool) {
        self.isAuthenticated = isAuthenticated
    }
}
