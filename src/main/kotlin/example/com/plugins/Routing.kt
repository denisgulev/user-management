package example.com.plugins

import example.com.application.dto.UserCreate
import example.com.application.dto.UserLogin
import example.com.application.dto.UserUpdate
import example.com.application.dto.UserWithTokenDto
import example.com.application.mappers.toDto
import example.com.application.mappers.toModel
import example.com.application.models.User
import example.com.application.services.tokens.TokenService
import example.com.application.services.users.IUserService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.application.ApplicationCallPipeline.ApplicationPhase.Plugins
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import org.koin.ktor.ext.inject

private val logger = KotlinLogging.logger {}

suspend fun ApplicationCall.isAuthorized(service: IUserService, vararg requiredRoles: User.Role): Boolean {
    val userId = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: return false
    val user = service.findUser(userId) ?: return false
    return user.role in requiredRoles
}

suspend fun ApplicationCall.hasPermission(service: IUserService, action: String): Boolean {
    val principal = principal<JWTPrincipal>()
    if (principal == null) {
        logger.warn { "JWTPrincipal is null, indicating failed JWT validation." }
        return false
    }
    val userId = principal.payload.getClaim("userId").asString()
    val user = service.findUser(userId) ?: return false

    return user.permission == User.Permission.ALL || user.permission == User.Permission.valueOf(action.uppercase())
}

fun Application.configureRouting() {
    val service by inject<IUserService>()
    val tokenService by inject<TokenService>()

    fun Route.adminOnlyRoute(path: String, build: Route.() -> Unit) {
        route(path) {
            intercept(Plugins) {
                if (!call.isAuthorized(service, User.Role.ADMIN)) {
                    call.respond(HttpStatusCode.Forbidden, "Insufficient permissions")
                    finish()
                }
            }
            build()
        }
    }

    routing {
        swaggerUI(path = "swagger-ui", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
        }
        get("/") {
            call.respondText("User management service!")
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

        authenticate {
            get("/users/has-permission/{action}") {
                val action = call.parameters["action"]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing action")

                if (call.hasPermission(service, action)) {
                    call.respond(HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Insufficient permissions")
                }
            }

            adminOnlyRoute("/users") {
                // Retrieve user by id
                get("/{id}") {
                    val id = call.parameters["id"]
                        ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing id")

                    service.findUser(id)?.let {
                        call.respond(it.toDto())
                    } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
                }

                // Update user
                patch("/{id}") {
                    val id = call.parameters["id"]
                        ?: return@patch call.respond(HttpStatusCode.BadRequest, "Missing id")
                    val userUpdate = call.receive<UserUpdate>()

                    service.findUser(id)?.let {
                        service.updateUser(id, it.copy(username = userUpdate.username, email = userUpdate.email))
                        call.respond(HttpStatusCode.OK)
                    } ?: call.respond(HttpStatusCode.NotFound, "No records found for id $id")
                }

                // Delete user
                delete("/{id}") {
                    val id = call.parameters["id"]
                        ?: return@delete call.respond(HttpStatusCode.BadRequest, "Missing id")

                    if (service.removeUser(id))
                        call.respond(HttpStatusCode.OK)
                    else
                        call.respond(HttpStatusCode.NotFound, "No records found for id $id")
                }

                // Retrieve all users
                get {
                    service.findAllUsers().toList()
                        .map { it.toDto() }
                        .let { call.respond(HttpStatusCode.OK, it) }
                }

                // Create user
                post {
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
            }
        }
    }
}
