package example.com.application.repository

import example.com.application.schema.User
import org.bson.BsonValue
import org.bson.types.ObjectId

interface IUserRepository {
    suspend fun insertOne(user: User): BsonValue?
    suspend fun findById(objectId: ObjectId): User?
}