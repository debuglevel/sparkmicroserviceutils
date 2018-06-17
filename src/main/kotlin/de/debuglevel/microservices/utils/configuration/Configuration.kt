package de.debuglevel.microservices.utils.configuration

import com.natpryce.konfig.*
import com.natpryce.konfig.ConfigurationProperties.Companion.systemProperties
import java.io.File

/**
 * Configuration of the microservice.
 *
 * Provides some usual configurations for a microservice (e.g. used port). Furthermore, the `configuration` variable
 * exposes access to any defined configuration parameter defined.
 *
 * There are multiple possible sources for configurations, where the former override the later:
 * - Java system properties
 * - Environment variables
 * - file `configuration.properties` on the working directory
 * - file `defaults.properties` in the class path
 *
 * For environment variables, "SERVER_PORT" can be used where "server.port" would actually be needed.
 */
object MicroserviceConfiguration {
    private val key_server_port = Key("server.port", intType)
    private val key_port = Key("port", intType)

    val configuration: Configuration

    init {
        var config: Configuration = systemProperties()

        config = config overriding
                EnvironmentVariables()

        config = config overriding
                ConfigurationProperties.fromOptionalFile(File("configuration.properties"))

        val defaultsPropertiesFilename = "defaults.properties"
        if (ClassLoader.getSystemClassLoader().getResource(defaultsPropertiesFilename) != null) {
            config = config overriding
                    ConfigurationProperties.fromResource(defaultsPropertiesFilename)
        }

        configuration = config
    }

    /**
     * Port on which the REST server listens.
     *
     * Defaults to 4567 if none is configured.
     */
    val port = configuration.getOrNull(key_port) ?: configuration.getOrNull(key_server_port) ?: 4567
}