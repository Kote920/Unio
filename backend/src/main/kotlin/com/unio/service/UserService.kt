package com.unio.service

import com.unio.entity.User
import com.unio.exception.UserAlreadyExistsException
import com.unio.exception.UserNotFoundException
import com.unio.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    fun findByFirebaseUid(uid: String): User {
        return userRepository.findByFirebaseUid(uid)
            ?: throw UserNotFoundException("User not found")
    }

    @Transactional
    fun createUser(firebaseUid: String, email: String, displayName: String?): User {
        if (userRepository.existsByFirebaseUid(firebaseUid)) {
            throw UserAlreadyExistsException("User already registered")
        }
        val user = User(
            firebaseUid = firebaseUid,
            email = email,
            displayName = displayName
        )
        return userRepository.save(user)
    }

    @Transactional
    fun getOrCreateUser(firebaseUid: String, email: String, displayName: String?): User {
        return userRepository.findByFirebaseUid(firebaseUid)
            ?: createUser(firebaseUid, email, displayName)
    }
}
