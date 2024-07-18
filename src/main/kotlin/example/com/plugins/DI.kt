package example.com.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI(environment: ApplicationEnvironment) {
    val environmentModule = module {
        single { environment }
    }

    install(Koin) {
        slf4jLogger()
        modules(
            environmentModule,
            DatabaseModule().module,
            defaultModule
        )
    }
}