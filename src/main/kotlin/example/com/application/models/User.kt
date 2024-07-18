package example.com.application.models

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import java.time.LocalDateTime

data class User(
    @BsonId
    val id: ObjectId,
    val username: String,
    val email: String,
    val password: String,
    val role: Role = Role.USER,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role {
        USER, ADMIN
    }
}
