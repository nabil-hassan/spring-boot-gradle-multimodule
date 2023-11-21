# Spring Boot Gradle multi-module project

Illustrates how to use a Gradle multi-module project to build multiple Spring Boot applications.

## Requirements

The fundamental requirements we have for this solution are:

* Should not repeat Spring Boot Gradle plugin in every module.
* Should not repeat common dependencies in every module.
* Should not repeat common Gradle configuration (Java versions, test platform) across every module.
* Each non-library module can be built into an independent Spring Boot application.
* Library modules containing common code we wish to share can be built and included in other modules.

## Implementation

* We must define our root project name and list of sub-projects in [settings.gradle](settings.gradle).

```
rootProject.name = 'spring-boot-gradle-multimodule'

include 'common'
include 'service-a'
include 'service-b'
```

* If we want to use a library module in one of the services, we do the following:

```
dependencies {
    implementation project(':common')
}
```

* To define shared configuration elements, within `buildSrc` we add two config files - one for [common Gradle project config](buildSrc/src/main/groovy/multimodule-config.gradle), and one for [Spring Boot](buildSrc/src/main/groovy/multimodule-spring-boot.gradle).

* The key thing to note here is that within the Spring Boot config, we **must not** include versions for the plugins.

```
plugins {
    id 'org.springframework.boot'
    id 'io.spring.dependency-management'
}
```

* Instead, we define the plugins as dependencies in the [build.gradle](buildSrc/build.gradle) for `buildSrc.

```
dependencies {
    implementation "org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}"
    implementation "io.spring.dependency-management:io.spring.dependency-management.gradle.plugin:${springDependencyPluginVersion}"
}
```

* The actual versions for the plugins are defined in the [gradle.properties](buildSrc/gradle.properties) file.

```
springBootVersion=3.1.3
springDependencyPluginVersion=1.1.3
```

* Then within each module, we can import the common configuration elements as plugins e.g. [service-a](service-a/build.gradle):

```
plugins {
    id 'multimodule-config'
    id 'multimodule-spring-boot'
}
```

### Adding dependencies for individual sub-modules

To add a dependency for a single sub-module only, open the sub-module's folder, and add a definition like:

Notice no explicit version is specified - the version is kept consistent via the Spring Boot dependency management plugins (see above).

```
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-aop'
}
```

### Caveat regarding library modules

* The `common` module is just library code, and not a real Spring Boot application.
* The initial configuration will thus cause the following error when you attempt to build the project:

```
./gradlew build

Execution failed for task ':common:bootJar'.
> Error while evaluating property 'mainClass' of task ':common:bootJar'.
   > Failed to calculate the value of task ':common:bootJar' property 'mainClass'.
      > Main class name has not been configured and it could not be resolved from classpath build/classes/java/main
```

* To overcome this, add the following directive to the [build.gradle](common/build.gradle) for the library module.

```
bootJar {
    enabled = false
}
```
