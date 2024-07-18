package example.com.plugins

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.application.config.ApplicationConfig
import io.ktor.server.application.*
import io.ktor.server.config.*
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class DatabaseModule() {
    @Single
    fun provideMongoClient(config: ApplicationConfig, environment: ApplicationEnvironment): MongoClient {
        return createMongoClient(config, environment)
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
 * * `db.mongo.host` host that will be used for the database connection
 * * `db.mongo.port` port that will be used for the database connection
 * * `db.mongo.maxPoolSize` maximum number of connections to a MongoDB server
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
    val password = config.applicationConfiguration.tryGetString("db.mongo.password")
    val host = config.applicationConfiguration.tryGetString("db.mongo.host") ?: "127.0.0.1"
    val port = config.applicationConfiguration.tryGetString("db.mongo.port") ?: "27017"
    val maxPoolSize = config.applicationConfiguration.tryGetString("db.mongo.maxPoolSize")?.toInt() ?: 20
    val databaseName = config.applicationConfiguration.tryGetString("db.mongo.database.name") ?: "myDatabase"

    val credentials = user?.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val uri = "mongodb://$credentials$host:$port/?maxPoolSize=$maxPoolSize&w=majority"

    val mongoClient = MongoClient.create(uri)
    environment.monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return mongoClient
}
