package example.com.application.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.application.schema.User
import kotlinx.coroutines.flow.firstOrNull
import org.bson.BsonValue
import org.bson.types.ObjectId

class UserRepositoryImpl(
    private val mongoDatabase: MongoDatabase
): IUserRepository {

    companion object {
        const val USER_COLLECTION = "user"
    }

    override suspend fun insertOne(user: User): BsonValue? {
        try {
            val result = mongoDatabase.getCollection<User>(USER_COLLECTION).insertOne(
                user
            )
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun findById(objectId: ObjectId): User? =
        mongoDatabase.getCollection<User>(USER_COLLECTION).withDocumentClass<User>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()
}