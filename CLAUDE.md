# Unio — Personal Finance Management Platform

## Overview
Multi-platform fintech app for the Georgian market. Connects multiple bank accounts, manages finances, analyzes spending, and provides AI-powered financial advice.

## Tech Stack
- **Backend:** Kotlin 2.1.0 + Spring Boot 3.4.1 + Gradle (Kotlin DSL) + Java 21
- **Android:** Kotlin 2.2.10 + Jetpack Compose (BOM 2026.02.01) + Dagger Hilt 2.56.2 + MVI
- **iOS:** Swift + SwiftUI + MVI (iOS 26.4 deployment target)
- **Database:** PostgreSQL 16 + Redis 7
- **Auth:** Firebase Authentication (email/password) — verified on backend via Firebase Admin SDK
- **Infra:** Docker Compose for local dev

## Project Structure
```
Unio/
├── backend/          # Spring Boot API (package: com.unio)
│   └── src/main/kotlin/com/unio/
│       ├── config/       # FirebaseConfig, SecurityConfig
│       ├── controller/   # REST controllers
│       ├── dto/          # Request/Response DTOs
│       ├── entity/       # JPA entities
│       ├── exception/    # Custom exceptions + GlobalExceptionHandler
│       ├── repository/   # Spring Data JPA repositories
│       ├── security/     # FirebaseAuthenticationFilter, FirebasePrincipal
│       └── service/      # Business logic
├── android/          # Android app (package: com.example.unio)
│   └── app/src/main/java/com/example/unio/
│       ├── data/
│       │   ├── remote/       # Retrofit API interfaces, DTOs, AuthInterceptor
│       │   └── repository/   # Repository implementations
│       ├── di/               # Hilt modules (FirebaseModule, NetworkModule, RepositoryModule)
│       ├── domain/
│       │   ├── model/        # Domain models
│       │   ├── repository/   # Repository interfaces
│       │   └── usecase/      # Use cases
│       └── presentation/
│           ├── auth/         # AuthContract (State/Intent/Effect), AuthViewModel, screens
│           ├── home/
│           └── navigation/   # NavGraph
├── ios/              # iOS app (bundle: Unio.Unio)
│   └── Unio/Unio/
│       ├── Data/
│       │   ├── Remote/       # AuthAPIClient (URLSession), DTOs
│       │   └── Repository/   # Repository implementations
│       ├── DI/               # DependencyContainer (manual, ObservableObject)
│       ├── Domain/
│       │   ├── Model/        # Domain models, AuthError
│       │   ├── Repository/   # Repository protocols
│       │   └── UseCase/      # Use cases
│       └── Presentation/
│           ├── Auth/         # AuthContract (State/Intent/Effect), AuthViewModel, views
│           ├── Home/
│           └── Navigation/   # AppRouter, RootView
├── docker/           # Docker Compose (PostgreSQL 16 + Redis 7)
└── docs/
```

## Architecture Rules

### General
- All API endpoints under `/api/v1/`
- Database changes only via Flyway migrations (naming: `V{n}__{description}.sql`)
- Never hardcode secrets — use environment configs or gitignored files

### Mobile — Both Platforms
- **MVI** (Model-View-Intent) pattern for presentation layer
- **Clean Architecture** with three layers:
  - **data/** — API clients, repository implementations, DTOs
  - **domain/** — models, repository interfaces (protocols), use cases
  - **presentation/** — ViewModels (State/Intent/Effect), UI screens/views
- Domain layer has NO dependency on data or presentation
- Repository interfaces defined in domain, implemented in data
- Use cases encapsulate single business operations with input validation
- ViewModels expose `State` (observable) and `Effect` (one-shot events)
- UI sends `Intent` to ViewModel, observes State changes

### Android-Specific
- **Dagger Hilt** for dependency injection (`@HiltAndroidApp`, `@AndroidEntryPoint`, `@HiltViewModel`)
- DI modules in `di/` package: `FirebaseModule`, `NetworkModule`, `RepositoryModule`
- Retrofit + OkHttp for networking; `AuthInterceptor` auto-attaches Firebase Bearer token
- Compose Navigation for screen routing
- `StateFlow` for state, `SharedFlow` for effects
- Base URL for emulator: `http://10.0.2.2:8080/`

### iOS-Specific
- **Manual DI container** (`DependencyContainer` as `ObservableObject`, passed via `.environmentObject()`)
- URLSession with async/await for networking (no third-party HTTP library)
- `@Published` properties for state and effects in `@MainActor` ViewModels
- SwiftUI navigation via `AppRouter` (ObservableObject with `isAuthenticated` flag)
- Xcode uses `PBXFileSystemSynchronizedRootGroup` — files on disk auto-included, no pbxproj editing needed
- Firebase iOS SDK via SPM (v12.12.1)

### Backend-Specific
- Spring Security: stateless, CSRF disabled, Firebase token verification via `FirebaseAuthenticationFilter`
- Filter verifies token only — does NOT perform DB lookup; controllers handle user creation/lookup
- Firebase Admin SDK initialized from `firebase-service-account.json` (classpath, gitignored)
- Spring profiles: `dev` (local Docker), `staging`, `prod` (env vars), `test` (create-drop, no Flyway)

## Auth Flow
1. Mobile: Firebase Auth `createUser`/`signIn` with email+password
2. Mobile: Gets Firebase ID token, sends as `Authorization: Bearer <token>`
3. Backend: `FirebaseAuthenticationFilter` verifies token → sets `FirebasePrincipal` in SecurityContext
4. Backend: Controller creates/finds `User` in PostgreSQL, returns `UserResponse`

## Build & Run Commands
```bash
# Docker (PostgreSQL + Redis)
cd docker && docker compose up -d

# Backend (requires Java 21)
cd backend && JAVA_HOME=$(/usr/libexec/java_home -v 21) ./gradlew bootRun

# Android
cd android && ./gradlew assembleDebug

# iOS
open ios/Unio/Unio.xcodeproj   # Build in Xcode (Cmd+B)
```

## Local Dev Credentials
- PostgreSQL: `localhost:5432`, db/user/pass: `unio/unio/unio`
- Redis: `localhost:6379`

## Conventions
- Backend package: `com.unio`
- Android package: `com.example.unio`
- iOS bundle: `Unio.Unio`
- Kotlin code style: official Kotlin conventions
- Swift code style: official Swift conventions
- Commit messages: concise, describe the "why"
