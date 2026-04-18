import Foundation

final class AuthAPIClient {
    private let baseURL: URL

    #if DEBUG
    init(baseURL: URL = URL(string: "http://localhost:8080")!) {
    #else
    init(baseURL: URL = URL(string: "https://unio-b329.onrender.com")!) {
    #endif
        self.baseURL = baseURL
    }

    func register(token: String, displayName: String?) async throws -> UserResponse {
        let body = RegisterRequest(displayName: displayName)
        return try await request(
            endpoint: "api/v1/auth/register",
            method: "POST",
            token: token,
            body: body
        )
    }

    func me(token: String) async throws -> UserResponse {
        return try await request(
            endpoint: "api/v1/auth/me",
            method: "GET",
            token: token,
            body: nil as RegisterRequest?
        )
    }

    private func request<T: Decodable, B: Encodable>(
        endpoint: String,
        method: String,
        token: String,
        body: B?
    ) async throws -> T {
        let url = baseURL.appendingPathComponent(endpoint)
        var request = URLRequest(url: url)
        request.httpMethod = method
        request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        if let body = body, method != "GET" {
            request.httpBody = try JSONEncoder().encode(body)
        }

        let (data, response) = try await URLSession.shared.data(for: request)

        guard let httpResponse = response as? HTTPURLResponse else {
            throw AuthError.unknown
        }

        guard (200...299).contains(httpResponse.statusCode) else {
            let message = String(data: data, encoding: .utf8) ?? "Server error"
            throw AuthError.serverError(message)
        }

        let decoder = JSONDecoder()
        return try decoder.decode(T.self, from: data)
    }
}
