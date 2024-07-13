package example.com.application.schema

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import org.jetbrains.annotations.NotNull

data class User(
    @BsonId
    val id: ObjectId? = null,
    val username: String? = null,
    val email: String? = null,
    val passwordHash: String? = null
) {
    fun toResponse() = UserResponse(
        id = id.toString(),
        username = username,
        email = email,
        passwordHash = passwordHash
    )
}

@Serializable
data class UserResponse(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val passwordHash: String? = null
)

@Serializable
data class UserCreate(
    @NotNull
    val username: String,
    @NotNull
    val email: String,
    @NotNull
    val password: String
) {
    fun toDomain() = User(
        id = ObjectId(),
        username = username,
        email = email,
        passwordHash = password
    )
}

@Serializable
data class UserUpdate(
    @NotNull
    val username: String,
    @NotNull
    val email: String
) {
    fun toDomain() = User(
        username = username,
        email = email
    )
}