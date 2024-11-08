package example.com.application.services.tokens

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import example.com.application.config.ApplicationConfig
import example.com.application.models.User
import example.com.application.repository.UserRepositoryImpl
import mu.KotlinLogging
import org.koin.core.annotation.Single
import java.util.*

/**
 * Token Exception
 * @property message String
 */
sealed class TokenException(message: String) : RuntimeException(message) {
    class InvalidTokenException(message: String) : TokenException(message)
}

@Single
class TokenService(
    private val myConfig: ApplicationConfig
) {

    companion object {
        private val logger = KotlinLogging.logger(UserRepositoryImpl::class.java.name)
    }

    // Read the jwt property from the config file if you are using EngineMain
    val audience by lazy {
        myConfig.applicationConfiguration.propertyOrNull("jwt.audience")?.getString() ?: "jwt-audience"
    }
    val realm by lazy {
        myConfig.applicationConfiguration.propertyOrNull("jwt.realm")?.getString() ?: "jwt-realm"
    }
    val issuer by lazy {
        myConfig.applicationConfiguration.propertyOrNull("jwt.issuer")?.getString() ?: "jwt-issuer"
    }
    val expiresIn by lazy {
        myConfig.applicationConfiguration.propertyOrNull("jwt.time")?.getString()?.toLong() ?: 3600000000
    }
    val secret by lazy {
        myConfig.applicationConfiguration.propertyOrNull("jwt.secret")?.getString() ?: "jwt-secret"
    }

    init {
        logger.debug { "Init tokens service with audience: $audience" }
    }

    /**
     * Generate a token JWT
     * @param user User
     * @return String
     */
    fun generateJWT(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withSubject("Authentication")
            // user claims and other data to store
            .withClaim("username", user.username)
            .withClaim("userId", user.id.toString())
            .withClaim("role", user.role.name)
            // expiration time from currentTimeMillis + (time in seconds) * 1000 (to millis)
            .withExpiresAt(Date(System.currentTimeMillis() + expiresIn * 1000L))
            // sign with secret
            .sign(
                Algorithm.HMAC512(secret)
            )
    }

    /**
     * Verify a token JWT
     * @return JWTVerifier
     * @throws TokenException.InvalidTokenException
     */
    fun verifyJWT(): JWTVerifier {
        return try {
            JWT.require(Algorithm.HMAC512(secret))
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
        } catch (e: Exception) {
            throw TokenException.InvalidTokenException("Invalid token")
        }
    }
}