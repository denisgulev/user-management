# user-management

1. Implement API to create and retrieve a user

// TODO
2. Add functionality to allow the login of a user with password and create a token for successful login

// TODO
3. Create an ADMIN user, which will be the only one to be able to create a user

// TODO
4. Containerize the application, use ARGS / ENV to receive variables for mongo connection and jwt info

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

    ```bash
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
   ```bash
   ./gradlew :buildFatJar 
   ```

4. **Run the application**:

    ```bash
    ./gradlew run
    ```

The application will start on `http://localhost:8080`.