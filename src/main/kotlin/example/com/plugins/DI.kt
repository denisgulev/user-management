package example.com.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import example.com.application.repository.UserRepositoryImpl
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ksp.generated.defaultModule
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureDI(environment: ApplicationEnvironment) {
    install(Koin) {
        slf4jLogger()
        modules(
            defaultModule,
            module {
                single {
                    MongoClient.create(
                        environment.config.propertyOrNull("db.mongo.url")?.getString()
                            ?: throw RuntimeException("Failed to access MongoDB URI.")
                    )
                }
                single {
                    get<MongoClient>().getDatabase(
                        environment.config.property("db.mongo.database.name").getString()
                    )
                }
            }, module {
                single<UserRepositoryImpl> { UserRepositoryImpl(get()) }
            }
        )
    }
}