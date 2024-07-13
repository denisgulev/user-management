package example.com.application.repository

import com.mongodb.MongoException
import com.mongodb.client.model.*
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.application.schema.User
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import org.bson.BsonValue
import org.bson.types.ObjectId

class UserRepositoryImpl(
    private val mongoDatabase: MongoDatabase
) : IUserRepository {

    companion object {
        const val USER_COLLECTION = "user"
    }

    val db = mongoDatabase.getCollection<User>(USER_COLLECTION)

    override suspend fun insertOne(user: User): BsonValue? {
        try {
            val result = db.insertOne(user)
            return result.insertedId
        } catch (e: MongoException) {
            System.err.println("Unable to insert due to an error: $e")
        }
        return null
    }

    override suspend fun findById(objectId: ObjectId): User? =
        db.withDocumentClass<User>()
            .find(Filters.eq("_id", objectId))
            .firstOrNull()

    override suspend fun findAll(): List<User> =
        db.withDocumentClass<User>()
            .find()
            .toList()

    override suspend fun update(objectId: ObjectId, user: User): User? =
        db.withDocumentClass<User>()
            .findOneAndUpdate(
                filter = Filters.eq("_id", objectId),
                update = Updates.combine(
                    Updates.set(User::username.name, user.username),
                    Updates.set(User::email.name, user.email)
                ),
                options = FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            )

    override suspend fun remove(objectId: ObjectId): Boolean =
        db.deleteOne(
            filter = Filters.eq("_id", objectId)
        ).deletedCount == 1L
}