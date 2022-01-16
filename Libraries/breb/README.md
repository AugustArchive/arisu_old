# breb
> 🍞🐾 **GraphQL library made in Kotlin to make your life easier and have it setup easily. :D**

## Why?
Like most developers, I don't think that the libraries out there suit my fancy of building GraphQL applications.

Well, what does? What suits my fancy is to use kotlinx.serialization and Ktor together! But, there are some limitations
when using libraries that are made for GraphQL and Kotlin like KGraphQL and GraphQL for Kotlin by Expedia Media. Which,
one or the other doesn't have what I want to build applications with, so why not I do it?

I was going to make this into a separate sub-project with [Tsubaki](https://github.com/arisuland/Tsubaki), but,
the scope of this will be kind-of large and unmaintainable in that project, so... it's a library! :D

I liked the way the GraphQL for JavaScript paired with [type-graphql](https://typegraphql.com/) worked out together, so I wanted to build
something similar but in Kotlin! Wish me luck...

## Modules
- [breb-core](./breb-core) **~** The core foundation for breb
- [breb-ktor](./breb-ktor) **~** Ktor plugin for breb
- [breb-validation](./breb-validation) **~** Validation hooks to plug into breb, to add some nice validation! :D

## License
**breb** is released under the **MIT** License. :3
