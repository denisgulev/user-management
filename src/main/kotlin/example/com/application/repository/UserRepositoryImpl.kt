package example.com.application.repository

import com.mongodb.MongoException
import com.mongodb.client.model.Filters
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.ReturnDocument
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import com.toxicbakery.bcrypt.Bcrypt
import example.com.application.config.ApplicationConfig
import example.com.application.models.User
import io.ktor.server.config.*
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import mu.KotlinLogging
import org.bson.BsonValue
import org.bson.types.ObjectId
import org.koin.core.annotation.Single

@Single
class UserRepositoryImpl(
    mongoDatabase: MongoDatabase,
    appConfig: ApplicationConfig
) : IUserRepository {

    companion object {
        private val logger = KotlinLogging.logger(UserRepositoryImpl::class.java.name)
    }

    private val usersCollection = appConfig.applicationConfiguration.tryGetString("db.mongo.database.name") ?: "users"
    private val db = mongoDatabase.getCollection<User>(usersCollection)

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

    override suspend fun checkUserNameAndPassword(username: String, password: String): User? {
        val user = findByUsername(username)
        return user?.let {
            if (Bcrypt.verify(password, user.password.encodeToByteArray())) {
                return user
            }
            return null
        }
    }

    override suspend fun findByUsername(username: String): User? =
        db.withDocumentClass<User>()
            .find(Filters.eq(User::username.name, username))
            .firstOrNull()
}