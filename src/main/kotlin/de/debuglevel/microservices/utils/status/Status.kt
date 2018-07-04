package de.debuglevel.microservices.utils.status

import mu.KotlinLogging
import spark.Spark
import spark.kotlin.get
import java.lang.management.ManagementFactory

private val logger = KotlinLogging.logger {}

var status = "up"

/**
 * Sets a path to /status/ which returns a JSON object about the current server status and environment information
 * @param clazz A class of the package to show the "version" and "title" attributes for (usually a class of your microservice).
 */
fun status(clazz: Class<*>) {
    logger.info("Activating path /status/")

    Spark.path("/status") {
        get("/") {
            logger.info("Got request for /status/")

            type(contentType = "application/json")
            buildStatusJson(clazz)
        }
    }
}

/**
 * @param clazz A class of the package to show the "version" and "title" attributes for.
 */
private fun buildStatusJson(clazz: Class<*>): String {
    return """
                {
                    "status":"$status",
                    "uptime":"${getUptime()}",
                    "version":"${Version.getVersion(clazz)}",
                    "title":"${Version.getTitle(clazz)}",
                    "jvmName":"${ManagementFactory.getRuntimeMXBean().name}",
                    "jvmSpecName":"${ManagementFactory.getRuntimeMXBean().specName}",
                    "jvmSpecVendor":"${ManagementFactory.getRuntimeMXBean().specVendor}",
                    "jvmSpecVersion":"${ManagementFactory.getRuntimeMXBean().specVersion}",
                    "vmName":"${ManagementFactory.getRuntimeMXBean().vmName}",
                    "vmVendor":"${ManagementFactory.getRuntimeMXBean().vmVendor}",
                    "vmVersion":"${ManagementFactory.getRuntimeMXBean().vmVersion}",
                    "compilerName":"${ManagementFactory.getCompilationMXBean().name}"
                }
            """.trimIndent()
}

/**
 * Gets the application uptime in seconds
 */
private fun getUptime() =
        ManagementFactory.getRuntimeMXBean().uptime / 1_000