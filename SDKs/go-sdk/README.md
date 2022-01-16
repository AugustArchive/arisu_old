# Go SDK for Arisu
> ☔☕ **Go client library to access Arisu's API**

## Installation
```shell
$ go get -u github.com/arisuland/go-sdk
```

## Usage
```go
package main

import (
	arisu "github.com/arisuland/go-sdk"
)

func main() {
	client := arisu.NewClient(&ClientOptions{
		BaseURL: "", // Base URL of Tsubaki (edit this to your domain if self-hosting)
        AccessKey: "", // Access key for authentication (https://docs.arisu.land/tsubaki/authentication#getting-access-key)
    })
	
	// Fetch a user
	client.users("noel").get()
	
	// GraphQL
	client.query("query { users(name: \"noel\") { name projects avatarUrl(size: 512) } }")
}
```

## Contributing
> If you want to contribute to this project, please read the [contributing guide](./.github/CONTRIBUTING.md).

## License
**Go SDK** is released under the **GPL-3.0** License. :3
