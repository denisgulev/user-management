package example.com.application.mappers

import example.com.application.dto.UserCreate
import example.com.application.dto.UserResponse
import example.com.application.models.User
import org.bson.types.ObjectId

fun User.toDto() = UserResponse(
    id = this.id.toString(),
    username = this.username,
    email = this.email,
    role = this.role,
    createdAt = this.createdAt.toString(),
    updatedAt = this.updatedAt.toString(),
    permission = this.permission
)

fun UserCreate.toModel() = User(
    id = ObjectId(),
    username = this.username,
    email = this.email,
    password = this.password,
    role = this.role,
    permission = this.permission
)
