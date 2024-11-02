# User Management

1. Implement API to create and retrieve a user 
   1. add also update and delete user APIs

2. Add functionality to allow the login of a user with password and create a token for successful login -> **~~TODO~~** **DONE**
   
   - added "/users/login" route to verify username/password correctness and generate a response with user's info and a token
   - implemented a TokenService to handle the creation of a token on successful login of a user
   - implemented inject functionality using Koin [koin-integration](./implementations/koin-integration.md)

3. Create an ADMIN user, which will be the only one to be able to create a user -> **~~TODO~~** **DONE**
   
   - endpoint "/users" with GET and POST operations were locked, requiring token + role authentication. See: [security](./implementations/security.md)

4. Containerize the application, use ARGS / ENV to receive variables for mongo connection and jwt info -> **~~TODO~~** **DONE**

---------

This microservice handles user authentication and authorization for your application using Ktor and MongoDB. It uses JWT tokens to secure endpoints and ensure that only authenticated users can perform certain actions. Additionally, only users with the ADMIN role are allowed to create new users.

## Features

- User Registration
- User Login
- JWT-based Authentication
- Role-based Authorization (only ADMIN can create new users)

## Getting Started

### Installation

1. **Clone the repository**:

    ```
    git clone https://github.com/denis.gulev/user-management.git
    cd user-management
    ```

2. **Setup `.env`**:

   Populate the variables in the `.env` file with your MongoDB user, password and database name.

3. **Update `init-mongo.js`**:

   Use the same database name inside "init-mongo.js" file.

   Visit [Bcrypt hashing](https://bcrypt.online/) to generate a hashed password for the admin user.

   **|** make sure to write down this password, as it will be used to login as the admin user

   Use the hash generated to replace **<ADMIN_HASH_PASSWORD>** in the "init-mongo.js" file.

### Build and Run

1. **Build and run the application using Docker Compose:**
    1. build the application as a fatJar
   ```
   ./gradlew :buildFatJar 
   ```

4. **Run the application**:

    ```
    docker compose up -d
    ```

The application will start on `http://localhost:8081`.


