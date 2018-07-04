package de.debuglevel.microservices.utils.status

object Version {
    /**
     * Gets the version (as defined in the MANIFEST.MF) of a package which contains a class.
     *
     * Depending on the method of creating a shadowed/fat jar, this may always be the value of the main package even if the class does not belong to the main package.
     *
     * @param clazz A class of the package to inspect
     */
    fun getVersion(clazz: Class<*>) = clazz.getPackage().implementationVersion ?: "unknown version"

    /**
     * Gets the title (as defined in the MANIFEST.MF) of a package which contains a class.
     *
     * Depending on the method of creating a shadowed/fat jar, this may always be the value of the main package even if the class does not belong to the main package.
     *
     * @param clazz A class of the package to inspect
     */
    fun getTitle(clazz: Class<*>) = clazz.getPackage().implementationTitle ?: "unknown title"
}