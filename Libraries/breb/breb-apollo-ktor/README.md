# breb-apollo-ktor
> **Ktor plugin to be compliant with [Apollo](https://github.com/apollographql/apollo-server)! Made with using [breb-core](../breb-core)**

## Usage
```kotlin
fun Application.module() {
    // Requires the breb-ktor package installed
    install(Breb)
    
    install(Apollo) {
        // ...
    }
}
```
