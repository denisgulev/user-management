package example.com.application.schema

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class User(
    @BsonId
    val id: ObjectId,
    val username: String,
    val email: String,
    val passwordHash: String
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
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String
)

@Serializable
data class UserRequest(
    val username: String,
    val email: String,
    val password: String
) {
    fun toDomain() = User(
        id = ObjectId(),
        username = username,
        email = email,
        passwordHash = password
    )
}
