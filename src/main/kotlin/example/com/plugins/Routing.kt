package example.com.plugins

import example.com.application.schema.UserCreate
import example.com.application.schema.UserUpdate
import example.com.application.service.IUserService
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val service by inject<IUserService>()

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
                val user = call.receive<UserCreate>()
                val id = service.createUser(user.toDomain())
                call.respond(HttpStatusCode.Created, "Created user with id $id")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        // Retrieve all users
        get("/users") {
            service.findAllUsers()
        }
        // Retrieve a single user
        get("/users/{id?}") {
            val id = call.parameters["id"]
                ?: return@get call.respondText(
                    text = "Missing id",
                    status = HttpStatusCode.BadRequest
                )

            service.findUser(id)?.let {
                call.respond(it.toResponse())
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
            service.updateUser(id, user.toDomain())?.let {
                call.respond(HttpStatusCode.OK)
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
    }
}
