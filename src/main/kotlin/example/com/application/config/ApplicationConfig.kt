package example.com.application.config

import io.ktor.server.config.ApplicationConfig
import org.koin.core.annotation.Single

/**
 * Application Configuration to encapsulate our configuration
 * from application.yaml or from other sources
 */
@Single
class ApplicationConfig {
    val applicationConfiguration: ApplicationConfig = ApplicationConfig("application.yaml")


    // We can set here all the configuration we want from application.yaml or from other sources
    // val applicationName: String = applicationConfiguration.property("ktor.application.name").getString()
    // val applicationPort: Int = applicationConfiguration.property("ktor.application.port").getString().toInt()
}