package de.debuglevel.microservices.utils.spark

import de.debuglevel.microservices.utils.configuration.MicroserviceConfiguration
import mu.KotlinLogging
import spark.kotlin.port

private val logger = KotlinLogging.logger {}

/**
 * Sets the port to a configured port.
 *
 * Currently, only the environment variable "PORT" is evaluated.
 */
fun configuredPort() {
    // TODO: Spark fails silently if port is already taken. There should be thrown an exception

    val port = MicroserviceConfiguration.port
    logger.info("Setting port to $port...")
    port(port)
    //logger.info("Setting port to $port succeeded.")
}