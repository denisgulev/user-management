package example.com

import example.com.plugins.*
import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureDI(environment)
    configureSecurity()
    configureRouting()

//    install(CallLogging) {
//        level = Level.INFO
//        format { call ->
//            val status = call.response.status()
//            val httpMethod = call.request.httpMethod.value
//            val authToken = call.request.headers["Authorization"]
//            "Status: $status, HTTP method: $httpMethod, Authorization: $authToken"
//        }
//    }
}
