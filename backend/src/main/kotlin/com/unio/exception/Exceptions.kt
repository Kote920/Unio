package com.unio.exception

class UserAlreadyExistsException(message: String) : RuntimeException(message)

class UserNotFoundException(message: String) : RuntimeException(message)
