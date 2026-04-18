package com.unio.controller

import com.unio.dto.RegisterRequest
import com.unio.dto.UserResponse
import com.unio.entity.User
import com.unio.security.FirebasePrincipal
import com.unio.service.UserService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val userService: UserService) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest,
        authentication: Authentication
    ): UserResponse {
        val principal = authentication.principal as FirebasePrincipal
        val user = userService.createUser(
            firebaseUid = principal.uid,
            email = principal.email,
            displayName = request.displayName
        )
        return user.toResponse()
    }

    @GetMapping("/me")
    fun me(authentication: Authentication): UserResponse {
        val principal = authentication.principal as FirebasePrincipal
        val user = userService.findByFirebaseUid(principal.uid)
        return user.toResponse()
    }

    private fun User.toResponse() = UserResponse(
        id = id!!,
        email = email,
        displayName = displayName,
        createdAt = createdAt
    )
}
