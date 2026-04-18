import SwiftUI
import FirebaseCore

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil) -> Bool {
        FirebaseApp.configure()
        return true
    }
}

@main
struct UnioApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    @State private var container = DependencyContainer()

    var body: some Scene {
        WindowGroup {
            RootView(container: container, router: AppRouter(isAuthenticated: container.authRepository.isLoggedIn))
        }
    }
}
