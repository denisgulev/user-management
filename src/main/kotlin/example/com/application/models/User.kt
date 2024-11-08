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
    val permission: Permission = Permission.VIEW,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    enum class Role(val role: String) {
        ADMIN("admin"),
        USER("user")
    }

    enum class Permission(val permission: String) {
        ALL("all"),
        VIEW("view"),
        CREATE("create"),
        DELETE("delete")
    }
}
