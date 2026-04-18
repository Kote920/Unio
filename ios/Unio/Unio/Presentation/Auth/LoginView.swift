import SwiftUI

struct LoginView: View {
    @ObservedObject var viewModel: AuthViewModel
    var onNavigateToRegister: () -> Void
    var onNavigateToHome: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Spacer()

            Text("Welcome Back")
                .font(.title)
                .fontWeight(.bold)

            Spacer().frame(height: 16)

            TextField("Email", text: Binding(
                get: { viewModel.state.email },
                set: { viewModel.handleIntent(.updateEmail($0)) }
            ))
            .textFieldStyle(.roundedBorder)
            .textContentType(.emailAddress)
            .keyboardType(.emailAddress)
            .textInputAutocapitalization(.never)
            .autocorrectionDisabled()

            SecureField("Password", text: Binding(
                get: { viewModel.state.password },
                set: { viewModel.handleIntent(.updatePassword($0)) }
            ))
            .textFieldStyle(.roundedBorder)
            .textContentType(.password)

            if let error = viewModel.state.error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.caption)
            }

            Button(action: {
                viewModel.handleIntent(.login)
            }) {
                if viewModel.state.isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                } else {
                    Text("Log In")
                        .frame(maxWidth: .infinity)
                }
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.state.isLoading)

            Button("Don't have an account? Register") {
                onNavigateToRegister()
            }
            .font(.footnote)

            Spacer()
        }
        .padding(24)
        .onChange(of: viewModel.effect) { _, effect in
            guard let effect else { return }
            switch effect {
            case .navigateToHome:
                onNavigateToHome()
            case .showError:
                break
            }
            viewModel.effect = nil
        }
    }
}
