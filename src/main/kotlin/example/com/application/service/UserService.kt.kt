package example.com.application.service

import example.com.application.repository.IUserRepository
import example.com.application.schema.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bson.types.ObjectId

class UserService(
    private val repository: IUserRepository
): IUserService {

    override suspend fun createUser(user: User): String = withContext(Dispatchers.IO) {
        repository.insertOne(user).toString()
    }

    override suspend fun findUser(id: String): User? = withContext(Dispatchers.IO) {
        println("ID as string: $id")
        println("ObjectId(id): ${ObjectId(id)}")
        repository.findById(ObjectId(id))
    }

//    // Update a user
//    suspend fun update(id: String, user: User): Document? = withContext(Dispatchers.IO) {
//        collection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), car.toDocument())
//    }
//
//    // Delete a user
//    suspend fun delete(id: String): Document? = withContext(Dispatchers.IO) {
//        collection.findOneAndDelete(Filters.eq("_id", ObjectId(id)))
//    }
}
