# Java SDK for Arisu
> ☔🎻 **Java SDK for Arisu's API, made in Java (with Android support)**

## Usage
```java
public class Main {
    public static void main(String[] args) {
        var sdk = new SDKBuilder()
                .setBaseEndpoint("https://api.arisu.land") // Sets the base API that points to Tsubaki, if self-hosting
                .withAccessToken("...") // The access token you get from the dashboard (https://arisu.land/developers)
                .fromHttpClient(new SDKHttpClient()) // use a custom http client instead, refer to the custom http client docs
                .build(); // Build a new SDK client, which has access to all APIs
        
        sdk.query("{ user(login: \"noel\") { login name avatarUrl(size: 1024) } }");
        // => QueryResult
        
        // Fetch a user using functions
        sdk.users().get("noel").returnAs("login", "name", new ReturningFunction<Number>("avatarUrl", 1024));
        // => User?
    }
}
```

## Installation
> Documentation: https://arisuland.github.io/java-sdk
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
    implementation("land.arisk.sdks:java-sdk:<VERSION>")
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
    implementation "land.arisk.sdks:java-sdk:<VERSION>"
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
        <groupId>land.arisk.sdks</groupId>
        <artifactId>java-sdk</artifactId>
        <version>{{VERSION}}</version>
        <type>pom</type>
    </dependency>
</dependencies>
```

## Custom HTTP Clients
The Java SDK allows you to use any popular http clients you want to use, instead of sticking with
OkHttp.

```kotlin
fun main(args: Array<String>) {
    // assuming you have ktor http + okhttp installed
    val client = HttpClient(OkHttp) {
        
    }
    
    val sdkClient = object SdkHttpClientBuilder {
        override fun request(options: SdkHttpRequestOptions): CompletableFuture<Any> {
            // do stuff here
        }
    }
    
    val sdk = SDKBuilder().apply {
        setBaseEndpoint("https://api.arisu.land")
        withAccessToken("...")
        fromHttpClient(sdkClient)
    }.build()
    
    sdk.users().get("noel")
}
```

## License
**Java SDK** is released under the MIT License. :3
