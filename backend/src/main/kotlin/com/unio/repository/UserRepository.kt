package com.unio.repository

import com.unio.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface UserRepository : JpaRepository<User, UUID> {
    fun findByFirebaseUid(firebaseUid: String): User?
    fun existsByFirebaseUid(firebaseUid: String): Boolean
}
