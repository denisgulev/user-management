package example.com.plugins

import example.com.application.dto.UserCreate
import example.com.application.dto.UserLogin
import example.com.application.dto.UserUpdate
import example.com.application.dto.UserWithTokenDto
import example.com.application.mappers.toDto
import example.com.application.mappers.toModel
import example.com.application.services.tokens.TokenService
import example.com.application.services.users.UserService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val service: UserService by inject()
    val tokenService: TokenService by inject()

    routing {
        swaggerUI(path = "swagger-ui", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        get("/") {
            call.respondText("User management service!")
        }
        // Create user
        post("/users") {
            try {
                val user = call.receive<UserCreate>().toModel()
                service.createUser(user).let {
                    call.respond(HttpStatusCode.Created, "Created user with id $it")
                }
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        // Retrieve all users
        get("/users") {
            service.findAllUsers()
            /*
                    val userId = call.principal<JWTPrincipal>()
                        ?.payload?.getClaim("userId")
                        .toString().replace("\"", "").toLong()

                    usersService.isAdmin(userId)
                        .onSuccess {
                            usersService.findAll().toList()
                                .map { it.toDto() }
                                .let { call.respond(HttpStatusCode.OK, it) }
                        }.onFailure {
                            handleUserError(it)
                        }
             */
        }
        // Retrieve a single user
        get("/users/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )

            service.findUser(id)?.let {
                call.respond(it.toDto())
            } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
        }
        // Update user
        patch("/users/{id?}") {
            val id = call.parameters["id"]
                ?: return@patch call.respondText(
                    text = "ID not found",
                    status = HttpStatusCode.NotFound
                )
            val user = call.receive<UserUpdate>()
            service.findUser(id)?.let {
                service.updateUser(id, it.copy(username = user.username, email = user.email))?.let {
                    call.respond(HttpStatusCode.OK)
                }
            } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
        }
        // Delete user
        delete("/users/{id?}") {
            val id = call.parameters["id"]
                ?: return@delete call.respondText(
                    text = "ID not found",
                    status = HttpStatusCode.NotFound
                )
            if (service.removeUser(id))
                call.respond(HttpStatusCode.OK)
            else
                call.respond(HttpStatusCode.NotFound, "No records found for id $id")
        }
        // Login with username and password
        post("/users/login") {
            try {
                val input = call.receive<UserLogin>()
                service.checkUserNameAndPassword(input.username, input.password)?.let { user ->
                    val token = tokenService.generateJWT(user)
                    call.respond(HttpStatusCode.OK, UserWithTokenDto(user.toDto(), token))
                }
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
