# spring-boot-gradle-multimodule

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



* The `common` module is just library code, and not a real Spring Boot application.
* The initial configuration will thus cause the following error when you attempt to build the project:

```
./gradlew build

Execution failed for task ':common:bootJar'.
> Error while evaluating property 'mainClass' of task ':common:bootJar'.
   > Failed to calculate the value of task ':common:bootJar' property 'mainClass'.
      > Main class name has not been configured and it could not be resolved from classpath build/classes/java/main
```

* To overcome this, 