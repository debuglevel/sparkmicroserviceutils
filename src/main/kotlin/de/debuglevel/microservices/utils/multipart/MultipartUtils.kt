package de.debuglevel.microservices.utils.multipart

import mu.KotlinLogging
import spark.Request
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import javax.servlet.MultipartConfigElement
import javax.servlet.http.Part

object MultipartUtils {
    private val logger = KotlinLogging.logger {}

    /**
     * Get the original file name of a multipart part
     */
    private fun getOriginalFilename(part: Part): String? {
        for (cd in part.getHeader("content-disposition").split(";")) {
            if (cd.trim { it <= ' ' }.startsWith("filename")) {
                return cd.substring(
                        cd
                                .indexOf('=') + 1)
                        .trim { it <= ' ' }
                        .replace("\"", "")
            }
        }

        return null
    }

    /**
     * Get text from a multipart request.
     *
     * @param fieldName name of the field in the HTML form
     */
    fun getField(request: Request, fieldName: String): String {
        setup(request)

        return request.raw()
                .getPart(fieldName)
                .inputStream
                .reader()
                .use { it.readText() }
    }

    fun getCheckbox(request: Request, fieldName: String): Boolean {
        setup(request)

        return request.raw()
                .parts
                .filter { it.name == fieldName }
                .filter { it.inputStream.reader().use { it -> it.readText() } == "on" }
                .any()
    }

    /**
     * Get a file from a multipart request.
     *
     * Copies the content form a multipart request field to a file and returns the path.
     * Note: The file should be deleted after usage.
     *
     * @param fieldName name of the field in the HTML form
     */
    fun getFile(request: Request, fieldName: String): Path {
        setup(request)

        val temporarySurveyFile = createTempFile("sparkmicroserviceutils").toPath()
        request.raw()
                .getPart(fieldName) // getPart needs to use same "name" as input field in form
                .inputStream
                .use {
                    Files.copy(it, temporarySurveyFile, StandardCopyOption.REPLACE_EXISTING)
                }
        logger.debug("Uploaded file '${getOriginalFilename(request.raw().getPart(fieldName))}' saved as '${temporarySurveyFile.toAbsolutePath()}'")
        return temporarySurveyFile
    }

    private fun setup(request: Request) {
        if (!request.attributes().contains("org.eclipse.jetty.multipartConfig")) {
            request.attribute("org.eclipse.jetty.multipartConfig", MultipartConfigElement("/temp"))
        }
    }
}