import SwiftUI

struct RegisterView: View {
    @ObservedObject var viewModel: AuthViewModel
    var onNavigateToLogin: () -> Void
    var onNavigateToHome: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Spacer()

            Text("Create Account")
                .font(.title)
                .fontWeight(.bold)

            Spacer().frame(height: 16)

            TextField("Display Name", text: Binding(
                get: { viewModel.state.displayName },
                set: { viewModel.handleIntent(.updateDisplayName($0)) }
            ))
            .textFieldStyle(.roundedBorder)
            .textContentType(.name)
            .autocorrectionDisabled()

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
            .textContentType(.newPassword)

            if let error = viewModel.state.error {
                Text(error)
                    .foregroundColor(.red)
                    .font(.caption)
            }

            Button(action: {
                viewModel.handleIntent(.register)
            }) {
                if viewModel.state.isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity)
                } else {
                    Text("Register")
                        .frame(maxWidth: .infinity)
                }
            }
            .buttonStyle(.borderedProminent)
            .disabled(viewModel.state.isLoading)

            Button("Already have an account? Log in") {
                onNavigateToLogin()
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
