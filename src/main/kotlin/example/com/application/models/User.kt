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
    val role: Role = Role.READ_ONLY,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role(val mongoRole: String) {
        READ_ONLY("readOnlyAccess"),         // Maps to MongoDB read-only role
        READ_WRITE("readWriteAccess"),       // Maps to MongoDB read-write role
        ADMIN("admin")                       // Maps to MongoDB admin role if needed
    }
}
