package example.com.application.service

import example.com.application.schema.User

interface IUserService {
    // Create new user
    suspend fun createUser(user: User): String
    // Find a user
    suspend fun findUser(id: String): User?
}