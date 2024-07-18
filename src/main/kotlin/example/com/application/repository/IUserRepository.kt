package example.com.application.repository

import example.com.application.models.User
import org.bson.BsonValue
import org.bson.types.ObjectId

interface IUserRepository {
    suspend fun insertOne(user: User): BsonValue?
    suspend fun findById(objectId: ObjectId): User?
    suspend fun findAll(): List<User>
    suspend fun update(objectId: ObjectId, user: User): User?
    suspend fun remove(objectId: ObjectId): Boolean
    suspend fun checkUserNameAndPassword(username: String, password: String): User?
    suspend fun findByUsername(username: String): User?
}