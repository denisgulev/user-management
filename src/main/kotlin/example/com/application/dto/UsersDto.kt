package example.com.application.dto

import example.com.application.models.User
import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class UserResponse(
    val id: String,
    val username: String,
    val email: String,
    val role: User.Role,
    val permission: User.Permission,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class UserCreate(
    @NotNull
    val username: String,
    @NotNull
    val email: String,
    @NotNull
    val password: String,
    val role: User.Role = User.Role.USER,
    val permission: User.Permission = User.Permission.VIEW
)

@Serializable
data class UserUpdate(
    @NotNull
    val username: String,
    @NotNull
    val email: String,
    @NotNull
    val permission: User.Permission
)

@Serializable
data class UserLogin(
    val username: String,
    val password: String
)

/**
 * User DTO for response with token
 */
@Serializable
data class UserWithTokenDto(
    val user: UserResponse,
    val token: String
)