# Arisu's Access Policy (DSL)

> 🌌 **minimal dsl language to implement access privileges**

## Introduction

Welcome to the [Arisu's Access Policy DSL](https://docs.arisu.land/acl-dsl) repository, glad you are here! This is a repository for the [Arisu's Access Policy](https://docs.arisu.land/acl-dsl) language. This covers the specification of the language, the syntax and the implementation. It also includes a reference implementation of the language in [Java / Kotlin](./java-impl) and [JavaScript](./js-impl).

Basically, you reference your **ACL** like in the ACL policy panel in your project or organization settings:

```
{
  # Wildcard (anyone who has access)
  * {
    allow {
      read
      write {
        if $currentUser.hasPermission(Permissions.WRITE)
      }
    }
  }

  # Specific user
  @user("noel") {
    allow {
      # Allow all permissions to `noel` in this project or organization
      *
    }
  }
}
```

It has a very simple syntax, which is inspired by the [GraphQL](https://en.wikipedia.org/wiki/GraphQL) syntax. You can also create extensions
to not repeat the same code:

```
{
  # ...
  @extension "awau" {
    * {
      allow {
        read
        write {
          if $currentUser.hasPermission(Permissions.WRITE)
        }
      }
    }
  }

  @apply "awau"

  # You can apply extensions from GitHub
  @apply {
    @extension(
      name: "awau",
      uri: "https://github.com/arisuland/awau-extensions"
    )
  }
}
```

You can view the specification of the language in the [Arisu's Access Policy](https://docs.arisu.land/acl-dsl) documentation.

## Why is this needed?

This is a very simple language, but it is still very useful to have a language to describe access policies and to implement them. It is also
an extendable language, which means you can add your own extensions to the language and use them in your projects!

## Maintained Implementations

| Language   | Implementation                        |
| ---------- | ------------------------------------- |
| Java       | [land.arisu.dsl:acl-dsl](./java-impl) |
| JavaScript | [@arisuland/acl-dsl](./js-impl)       |

Wrote your own implementation? Open a pull request and add it to the list!

## License

**acl-dsl** is released under the **MIT** License, read the [LICENSE](./LICENSE) file for more information.
