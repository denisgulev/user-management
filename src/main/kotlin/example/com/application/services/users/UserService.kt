package example.com.application.services.users

import com.toxicbakery.bcrypt.Bcrypt
import example.com.application.repository.IUserRepository
import example.com.application.models.User
import example.com.application.repository.UserRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mu.KotlinLogging
import org.bson.types.ObjectId
import org.koin.core.annotation.Single

@Single
class UserService(
    private val repository: IUserRepository
): IUserService {

    companion object {
        private const val BCRYPT_SALT = 12
        private val logger = KotlinLogging.logger(UserRepositoryImpl::class.java.name)
    }

    override suspend fun createUser(user: User): String = withContext(Dispatchers.IO) {
        val userToSave = user.copy(password = Bcrypt.hash(user.password, BCRYPT_SALT).decodeToString())
        repository.insertOne(userToSave).toString()
    }

    override suspend fun findUser(id: String): User? = withContext(Dispatchers.IO) {
        logger.debug("ID as string: {}", id)
        logger.debug("ObjectId(id): {}", ObjectId(id))
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

    /**
     * Check if username and password are valid
     * @param username String Username of user
     * @param password String Password of user
     * @return Result<User, UserError> Result of user or error if not found
     */
    override suspend fun checkUserNameAndPassword(username: String, password: String): User? = withContext(Dispatchers.IO) {
        val user = repository.findByUsername(username)
        return@withContext user?.let {
            if (Bcrypt.verify(password, user.password.encodeToByteArray())) {
                return@withContext user
            }
            return@withContext null
        }
    }
}
