package de.debuglevel.microservices.utils.apiversion

import spark.Spark.path

/**
 * Prefixes the nested paths with an API version identifier.
 * e.g. supplying "2" as version will publish the nested path "/picture" as
 * "http://localhost:4567/v2/picture".
 *
 * @param version version of the API
 * @param default if default is true, nested paths will also be published on a URL without version identifier (i.e. "http://localhost:4567/picture")
 * @param routeGroup see Spark path() call
 */
fun apiVersion(version: String, default: Boolean = false, routeGroup: () -> Unit) {
    path("v$version", routeGroup)

    if (default) {
        path("", routeGroup)
    }
}