# Serializable java.util.function Interfaces
This library contains serializable versions of generic functional interface introduced in JDK 8.

Interfaces in this library follow simple naming convention:
```
Serializable<original_name>
```

## Maven

The library has been deployed to Maven Central. Add the following dependency to you project:

### JDK 9
```
<dependencies>
    <dependency>
        <groupId>org.danekja</groupId>
        <artifactId>jdk-serializable-functional</artifactId>
        <version>1.9.0</version>
    </dependency>
</dependencies>
```

### JDK 8
```
<dependencies>
    <dependency>
        <groupId>org.danekja</groupId>
        <artifactId>jdk-serializable-functional</artifactId>
        <version>1.8.6</version>
    </dependency>
</dependencies>
```

## Component Model

Starting with version 1.9.0, the library is also a Java module.
Starting with version 1.8.4, the library is also an OSGi bundle.

## Versioning
The version identifier follows semantic scheme *major.minor.micro*, where *major* and *minor* portions
identify JDK version and micro is used for this library versioning (documentation/build modifications, etc.).

i.e. version **1.8.1** is the second release of this library based on JDK 1.8.  

## Licensing
The library is licensed under the MIT License.