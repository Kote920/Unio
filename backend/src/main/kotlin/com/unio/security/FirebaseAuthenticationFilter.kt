package com.unio.security

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class FirebaseAuthenticationFilter(
    private val firebaseAuth: FirebaseAuth
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val token = authHeader.substring(7)
            try {
                val decodedToken = firebaseAuth.verifyIdToken(token)
                val principal = FirebasePrincipal(
                    uid = decodedToken.uid,
                    email = decodedToken.email ?: "",
                    name = decodedToken.name
                )
                val authentication = UsernamePasswordAuthenticationToken(
                    principal, null, emptyList()
                )
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: FirebaseAuthException) {
                // Invalid token — continue without authentication, Spring Security will reject
            }
        }

        filterChain.doFilter(request, response)
    }
}
