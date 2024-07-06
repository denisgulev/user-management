package example.com.plugins

import example.com.application.schema.UserRequest
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
                val user = call.receive<UserRequest>()
                val id = service.createUser(user.toDomain())
                call.respond(HttpStatusCode.Created, "Created user with id $id")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
        // Read user
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
//        // Update user
//        put("/users/{id}") {
//            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
//            val car = call.receive<User>()
//            carService.update(id, car)?.let {
//                call.respond(HttpStatusCode.OK)
//            } ?: call.respond(HttpStatusCode.NotFound)
//        }
//        // Delete user
//        delete("/users/{id}") {
//            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
//            carService.delete(id)?.let {
//                call.respond(HttpStatusCode.OK)
//            } ?: call.respond(HttpStatusCode.NotFound)
//        }
    }
}
