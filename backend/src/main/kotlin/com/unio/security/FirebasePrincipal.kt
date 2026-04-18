package com.unio.security

data class FirebasePrincipal(
    val uid: String,
    val email: String,
    val name: String?
)
