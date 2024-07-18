### Koin Integration

To provide a seamless usage of dependency injection, in this project we integrated Koin 
framework with its Annotations features.

Steps followed:
1. Adding Plugins and Dependencies
   1. insert the following inside '**gradle.properties**' file and specifying the version
        ```
        koin_ksp_version=
        ```
   2. insert this in '**build.gradle.kts**' file
        ```
        val koin_ksp_version: String by project
        
        plugins {
          ....
          id("com.google.devtools.ksp") version "2.0.0-1.0.23"
        }
      
        dependencies {
          ....
          implementation("io.insert-koin:koin-annotations:$koin_ksp_version") // Koin Annotations for KSP
          ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
        }
        
        ksp {
          arg("koin.generated", "true")
        }
        ```
2. Annotate the classes that you want to be able to inject
   1. add **@Single** on classes to declare them as singleton
   2. add **@Module** on classes to declare them as modules, inside which we can define multiple 
      **@Single** functions to be instantiated as singletons
3. Configure Koin as a plugin of the application
   ```
   import org.koin.ksp.generated.*
   
   fun Application.configureDI() {
     install(Koin) {
        slf4jLogger()
        modules(
            DatabaseModule().module,
            defaultModule
        )
     }
   }
   ```
   In here we focus on these main points:
   1. the import indicates where the built modules (i.e. compiled singletons) are stored inside the project
   2. **DatabaseModule().module** -> refers to a class annotated with **@Module**
   3. **defaultModule** -> refers to a group of compiled singletons, group under a common name