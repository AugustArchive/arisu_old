# breb-validation
> Validation hooks for **breb** when constructing queries and mutations

## Usage
```kotlin
data class SomeDataClass(
    @ValidateLength(0, 10)
    val key: String
)
```

When you try to query:

```json
{
  "errors": [
    {
      "key": "VALIDATION_ERROR",
      "query": "query { key }",
      "message": "Message was over 10 characters. >:(",
      "extensions": [
        {
          "located_at": "SomeDataClass->key",
          "package": "land.arisu.breb.examples.DataPayloads"
        }
      ]
    }
  ],
  "data": {
    "key": null
  }
}
```

## Installation
> Documentation: https://breb.arisu.land/validation | JavaDoc: https://arisuland.github.io/breb
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
    implementation("land.arisu.breb:breb-validation:<VERSION>")
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
    implementation "land.arisu.breb:breb-validation:<VERSION>"
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
        <artifactId>breb-validation</artifactId>
        <version>{{VERSION}}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```
