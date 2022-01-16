# breb-core
> Core implementation of breb, with its own GraphQL engine

## Usage
```kotlin
fun main(args: Array<String>) {
    val gqlSchema = buildSchema(
        listOf(MyResolver(), AnotherResolver())
    )
    
    val graphql = graphQLEngine {
        schema = gqlSchema
    }
    
    // => Playground HTML
    graphql.renderPlayground("")
    
    // => If you prefer GraphiQL
    graphql.renderIDE("")
    
    // => Schema execution
    gqlSchema.execute(
        "query { hello { name login } }",
        variables = buildJsonObject {
            put("a", "b")
        },
    
        operationName = "..."
    )
}
```

## Installation
> Documentation: https://breb.arisu.land/core | Kotlin Docs: https://arisuland.github.io/breb
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
    implementation("land.arisu.breb:breb-core:<VERSION>")
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
    implementation "land.arisu.breb:breb-core:<VERSION>"
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
        <artifactId>breb-core</artifactId>
        <version>{{VERSION}}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```
