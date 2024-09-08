# User Management

1. Implement API to create and retrieve a user 
   1. add also update and delete user APIs

2. Add functionality to allow the login of a user with password and create a token for successful login -> **~~TODO~~** **DONE**
   
   - added "/users/login" route to verify username/password correctness and generate a response with user's info and a token
   - implemented a TokenService to handle the creation of a token on successful login of a user
   - implemented inject functionality using Koin [koin-integration](./implementations/koin-integration.md)

3. Create an ADMIN user, which will be the only one to be able to create a user -> **~~TODO~~** **DONE**
   
   - endpoint "/users" with GET and POST operations were locked, requiring token + role authentication. See: [security](./implementations/security.md)

4. Containerize the application, use ARGS / ENV to receive variables for mongo connection and jwt info -> **TODO**

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

2. **Setup MongoDB**:
   Ensure you have MongoDB installed and running. You can use a local MongoDB instance or a cloud service like MongoDB Atlas.

3. **Configure the application**:
   Update the MongoDB connection string and JWT secret in `application.yaml` or directly in the code.

### Build and Run

1. **Build and run the application using Docker Compose:**
    1. build the application as a fatJar
   ```
   ./gradlew :buildFatJar 
   ```

4. **Run the application**:

    ```
    ./gradlew run
    ```

The application will start on `http://localhost:8080`.


