package example.com.plugins

import example.com.application.services.tokens.TokenException
import example.com.application.services.tokens.TokenService
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {
    val jwtService by inject<TokenService>()

    authentication {
        jwt {
            realm = jwtService.realm
            // verify a token format and its signature
            verifier(jwtService.verifyJWT())
            validate { credential ->
                if (credential.payload.audience.contains(jwtService.audience)
                    && credential.payload.getClaim("username").asString().isNotEmpty()
                    )
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            // challenge configures a response to be sent back in case authentication fails
            challenge { defaultSchema, realm ->
                throw TokenException.InvalidTokenException("Token is not valid or has expired")
            }
        }
    }
}
