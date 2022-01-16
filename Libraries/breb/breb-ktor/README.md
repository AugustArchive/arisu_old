# breb-ktor
> Implements **breb-core** with Ktor!

## Usage
```kotlin
fun Application.module() {
    install(Breb) {
        // ...
    }
}
```

## Installation
> Documentation: https://breb.arisu.land/ktor | Kotlin Docs: https://arisuland.github.io/breb
>
> Version: 1.0.0

## Gradle
### Kotlin DSL
```kotlin
repositories {
    maven {
        url = uri("https://maven.arisu.land/repo/releases")
    }
}

dependencies {
    implementation("land.arisu.breb:breb-ktor:<VERSION>")
}
```

### Groovy DSL
```groovy
repositories {
    maven {
        url "https://maven.arisu.land/repo/releases"
    }
}

dependencies {
    implementation "land.arisu.breb:breb-ktor:<VERSION>"
}
```

## Maven
```xml
<repositories>
    <repository>
        <id>arisu-maven</id>
        <url>https://maven.arisu.land/repo/releases</url>
    </repository>
</repositories>
```

```xml
<dependencies>
    <dependency>
        <groupId>land.arisu.breb</groupId>
        <artifactId>breb-ktor</artifactId>
        <version>{{VERSION}}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```
