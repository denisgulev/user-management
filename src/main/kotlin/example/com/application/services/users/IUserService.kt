package example.com.application.services.users

import example.com.application.models.User

interface IUserService {
    /**
     * Create a new user
     * @param user to be created
     * @return id of the created user
     */
    suspend fun createUser(user: User): String
    /**
     * Retrieve a user
     * @param id of the user to be retrieved
     * @return the created user
     */
    suspend fun findUser(id: String): User?
    /**
     * Retrieve all users
     * @return a list of users
     */
    suspend fun findAllUsers(): List<User>
    /**
     * Update a used identified by id
     * @param id of the user
     * @param user info to be updated
     * @return the updated user
     */
    suspend fun updateUser(id: String, user: User): User?
    /**
     * Remove a user identified by id
     * @param id of the user
     * @return true if the user is removed, false if the user is not found
     */
    suspend fun removeUser(id: String): Boolean
    /**
     * Check if username and password are correct
     * @param username Username
     * @param password Password
     * @return User? User if username and password are correct, null otherwise
     */
    suspend fun checkUserNameAndPassword(username: String, password: String): User?

    suspend fun isAdmin(id: String): Boolean
}