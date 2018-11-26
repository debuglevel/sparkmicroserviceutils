package de.debuglevel.microservices.utils.logging

import mu.KotlinLogging
import spark.Request
import spark.Response
import java.nio.charset.Charset
import java.time.LocalDateTime

private val logger = KotlinLogging.logger {}

fun buildRequestLog(request: Request): String {
    val accept = request.headers("accept") ?: "<UNKNOWN>"

    return "Handling ${request.protocol()} ${request.requestMethod()} ${request.url()} from ${request.ip()} accepting $accept ..."
}

fun buildResponseLog(request: Request, response: Response): String {
    val raw = response.raw()

    val accept = request.headers("accept") ?: "<UNKNOWN>"
    val statuscode = raw.status
    val contentType = raw.getHeader("content-type") ?: "<UNKNOWN>"

    request.raw()

    val size = try {
        response.body().toByteArray(Charset.forName(raw.characterEncoding)).size.toString() + " bytes"
    } catch (e: Exception) {
        "unknown bytes"
    }

    return "Handled ${request.protocol()} ${request.requestMethod()} ${request.url()} from ${request.ip()} accepting $accept with status $statuscode content-type $contentType size $size..."
}

fun buildCommongLogFormat(request: Request, response: Response): String {
    val raw = response.raw()

    val size = try {
        response.body().toByteArray(Charset.forName(raw.characterEncoding)).size.toString()
    } catch (e: Exception) {
        "-"
    }

    // Common Log Format requires strftime format "%d/%b/%Y:%H:%M:%S %z" resulting in stupid "[10/Oct/2000:13:55:36 -0700]". Using ISO instead for now.
    val datetime = "[${LocalDateTime.now()}]"

    val ip = request.ip()
    val useridentifier = "-"
    val userid = "-"
    val requestline = "\"${request.requestMethod()} ${request.url()} ${request.protocol()}\""
    val statuscode = raw.status

    // add accept header as additional column
    val accept = request.headers("accept") ?: "-"

    // add returned content type as additional column
    val contentType = raw.getHeader("content-type") ?: "-"

    return "$ip $useridentifier $userid $datetime $requestline $statuscode $size $accept $contentType"
}