# Microservices.Utils

## Configuration

The `MicroserviceConfiguration` object provides access to values set in various sources of configurations. As of version 0.0.12, values are read from the following sources, whereas the former override the later:

1. Java system properties (e.g. `java -Dserver.port=1234`)
2. Environment variables (e.g. `SERVER_PORT=1234`)
3. `configuration.properties` file in the working directory (e.g. `server.port=1234`)
4. `defaults.properties` file in the class path (e.g. `server.port=1234`)

### Predefined configuration keys

A few configuration keys are used by Microservices.Utils itself and therefore predefined. Those can be access as read-only properties of the `MicroserviceConfiguration` object (e.g. `MicroserviceConfiguration.port`)

### Custom configuration keys

Own configuration keys can be access like `MicroserviceConfiguration.configuration[Key("port", intType)]` or `MicroserviceConfiguration.configuration.getOrNull(Key("port", intType))`.


## Spark
### Set port to configured number
Calling `configuredPort()` sets the port of the web server to the configured port number (`port` or `server.port` as of version 0.0.12).

## Status endpoint
Calling `status()` activates a status/health/diagnostics JSON object on `<HOST>/status/`.
The JSON object looks like this as of version 0.0.12:
```
{
    "status":"up",
    "uptime":"32",
    "jvmName":"6428@puck",
    "jvmSpecName":"Java Virtual Machine Specification",
    "jvmSpecVendor":"Oracle Corporation",
    "jvmSpecVersion":"1.8",
    "vmName":"Java HotSpot(TM) 64-Bit Server VM",
    "vmVendor":"Oracle Corporation",
    "vmVersion":"25.131-b11",
    "compilerName":"HotSpot 64-Bit Tiered Compilers"
}
```

## Build and Publish
Developer's notes on how to publish this artifact on bintray via PowerShell:
* ensure to increment version in `gradle.build`
* `$env:BINTRAY_USER = "debuglevel"; $env:BINTRAY_API_KEY = "SUPER_SECRET"; ./gradlew bintrayUpload`