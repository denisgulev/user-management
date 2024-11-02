package example.com.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.application.config.ApplicationConfig
import io.ktor.server.application.*
import io.ktor.server.config.*
import mu.KotlinLogging
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

private val logger = KotlinLogging.logger {  }

@Module
class DatabaseModule() {
    @Single
    fun provideMongoClient(config: ApplicationConfig, environment: ApplicationEnvironment): MongoClient {
        return  createMongoClient(config, environment)
    }

    @Single
    fun provideMongoDatabase(mongoClient: MongoClient, config: ApplicationConfig): MongoDatabase {
        return mongoClient.getDatabase(config.applicationConfiguration.tryGetString("db.mongo.database.name") ?: "myDatabase")
    }
}

/**
 * Establishes connection with a MongoDB database.
 *
 * The following configuration properties (in application.yaml/application.conf) can be specified:
 * * `db.mongo.user` username for your database
 * * `db.mongo.password` password for the user
 * * `db.mongo.url` url that will be used for the database connection
 * * `db.mongo.database.name` name of the database
 *
 * IMPORTANT NOTE: in order to make MongoDB connection working, you have to start a MongoDB server first.
 * See the instructions here: https://www.mongodb.com/docs/manual/administration/install-community/
 * all the parameters above
 *
 * @returns [MongoClient] instance
 * */
fun createMongoClient(config: ApplicationConfig, environment: ApplicationEnvironment): MongoClient {
    val user = config.applicationConfiguration.tryGetString("db.mongo.user")
    logger.info("**** DB_USER - $user")
    val password = config.applicationConfiguration.tryGetString("db.mongo.password")
    logger.info("**** DB_PASSWORD - $password")
    val url = config.applicationConfiguration.tryGetString("db.mongo.url")
    val databaseName = config.applicationConfiguration.tryGetString("db.mongo.database.name") ?: "users"
    logger.info("**** DB_NAME - $databaseName")

    val mongoClient = MongoClient.create(url ?: throw RuntimeException("Failed to access MongoDB URI."))
    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return mongoClient
}
