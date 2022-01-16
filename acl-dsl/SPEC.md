# Access Policy DSL ~ Specification

> You can view a prettified version of this document [here](https://docs.arisu.land/acl-dsl/specification)
>
> Last Modified: 26/07/2021

## Table of Contents

- [1. Introduction](#introduction)
- [2. Language](#language)
- [3. Syntax](#syntax)
  - [3.1 Root](#root)
  - [3.2 Operations](#operations)
- [4. Tokenization](#tokenization)
- [5. Lexical Analyser](#lexical-analyser)
- [6. Parser](#parser)
- [7. Syntax Tree](#syntax-tree)

## 1. Introduction

Hello! This is the specification details of the Access Policy Domain Specific Language (acl-dsl), used in production by Arisu to handle
access policies within projects and organizations to have a bit of a more structured approach to managing access policies.

This was built by the founder of Arisu, **Noel**. So this is his first attempt at writing a specification for a language and language
details are opioniated and subject to change in the near future, if needed.

## 2. Language

The language is a block-style language like **JSON** or **GraphQL**, so it is very easy to learn and use if you are familiar with those and are a
new-comer when creating access policies at a project or organization level. You start the implementation with braces:

```js
{
  // ...
}
```

## 3. Syntax

The syntax is very simple and straight forward, it is inspired by the **GraphQL** syntax.

### 3.1. Root

The root of the language is a **root** object, which can contain **operations** on a **resource**, such as a project or organization. The resource isn't specified since it is implied by the operations.

### 3.2. Operations

A list of operations is defined by the condition of the operation, which is a **condition** object. The **condition** object contains a **field**, which is the field to be checked, and a **value**, which is the value to be checked against.

Operations are wrapped in `@` symbols, which is a **key** of the operation you want to use.

```js
{
  // Represents a extension operation, which can be used on the condition.
  // It can have comments, with `//` or `#`.

  // Extensions can have names, which are used to identify the extension. You can do "" or '' if the name contains spaces.
  @extension myExtension {

  }

  // Represents a normal condition within this policy.
  @condition {
    // If the `$field` is a `$type` then the condition will be true if the value of the field is equal to the value of the condition.
    // To check if it's not equal, use `is:not` instead of `is`.
    if $field is $value {}
  }
}
```

## 4. Tokenization

This is the first step of the parsing process, which tokenizes the language into a list of **tokens**. A token is a representation of any value
in the language.

- `<SOF>` indicates the start of the file.
- `<EOF>` indicates the end of the file.
