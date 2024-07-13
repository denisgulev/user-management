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

    override suspend fun findAllUsers(): List<User> = withContext(Dispatchers.IO) {
        repository.findAll()
    }

    override suspend fun updateUser(id: String, user: User): User? = withContext(Dispatchers.IO) {
        repository.update(ObjectId(id), user)
    }

    override suspend fun removeUser(id: String): Boolean = withContext(Dispatchers.IO) {
        repository.remove(ObjectId(id))
    }
}
