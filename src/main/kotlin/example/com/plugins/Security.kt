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
                println("Validating JWT - Audience: ${credential.payload.audience}")
                println("Validating JWT - Claims: ${credential.payload.claims}")

                // Validate audience, issuer, and userId claim
                if (credential.payload.audience.contains(jwtService.audience) &&
                    credential.payload.issuer == jwtService.issuer &&
                    credential.payload.getClaim("userId").asString().isNotBlank()
                ) {
                    JWTPrincipal(credential.payload)
                } else {
                    null // Invalid JWT, unauthorized
                }
            }
            // challenge configures a response to be sent back in case authentication fails
            challenge { _, _ ->
                throw TokenException.InvalidTokenException("Invalid or expired token")
            }
        }
    }
}
