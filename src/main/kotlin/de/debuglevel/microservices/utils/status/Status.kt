package de.debuglevel.microservices.utils.status

import mu.KotlinLogging
import org.eclipse.jetty.util.Uptime.getUptime
import spark.Spark
import spark.kotlin.get
import java.lang.management.ManagementFactory

private val logger = KotlinLogging.logger {}

var status = "up"

/**
 * Sets a path to /status/ which returns a JSON object about the current server status and environment information
 */
fun status() {
    logger.info("Activating path /status/")

    Spark.path("/status") {
        get("/") {
            logger.info("Got request for /status/")

            type(contentType = "application/json")
            buildStatusJson()
        }
    }
}

private fun buildStatusJson(): String {
    return """
                {
                    "status":"$status",
                    "uptime":"${getUptime()}",
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
        ManagementFactory.getRuntimeMXBean().uptime / 1000