# GraphQL Scalars for [kotlinx.datetime](https://github.com/Kotlin/kotlinx-datetime)
> 🥑 **Adds scalar support for kotlinx.datetime for GraphQL Java**

## Why?
There is no library for supporting kotlinx.datetime, which we use in [Tsubaki](https://github.com/arisuland/Tsubaki),
and it'll be useful for anyone who wants to integrate kotlinx.datetime with GraphQL. :3

## Usage
```kotlin
fun main(args: Array<String>) {
    // RuntimeWiring is from graphql-java
    RuntimeWiring
        .newRuntimeWiring()
        .scalar(DatetimeScalars.INSTANT)
}
```

```graphql
scalar Date

type Something {
    date: Date!
}
```

## Installation
> Documentation: https://arisuland.github.io/datetime-scalars
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
    implementation("land.arisu.libraries:datetime-scalars:<VERSION>")
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
    implementation "land.arisu.libraries:datetime-scalars:<VERSION>"
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
        <groupId>land.arisu.libraries</groupId>
        <artifactId>datetime-scalars</artifactId>
        <version>{{VERSION}}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```
