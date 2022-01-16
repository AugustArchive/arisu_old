<div align="center">
  <h3>Tsubaki 🥀</h3>
  <blockquote>Core backend infrastructure for Arisu, all the magic begins here ✨🚀</blockquote>
</div>

<div align="center">
  <img alt="GitHub Workflow Status (master)" src="https://img.shields.io/github/workflow/status/arisuland/Tsubaki/ktlint/master?style=flat-square" />
  <img alt="GitHub Workflow Status (staging)" src="https://img.shields.io/github/workflow/status/arisuland/Tsubaki/ktlint/staging?style=flat-square" />
  <img alt="GitHub" src="https://img.shields.io/github/license/arisuland/Tsubaki?style=flat-square" />
</div>

<hr />

## Features
- 🌺 **Open Source and Free** — Every component is open source and free for everyone to use.
- 🏘️ **Multi-translations** — House multiple translation projects into one project.
- 🌟 **No Telemetry** — Tsubaki does not collect any data about your usage, if you're unsure, you can always view the source code and [metrics dashboard](https://metrics.arisu.land).
- 💻 **Performance** — Tsubaki is built with performance and functionality in mind to make workflows as smooth as possible.
- ⚡ **Robust** — Arisu makes your workflow way easier and manageable without any high latency.

## Packages
> Some other subprojects you can checkout that help Arisu how you see it today. :D
>
> If the projects return a **`404`** status code, it means that the project is not yet ready for public use.

|Name|Description|Status|
|----|-----------|------|
|☔ [arisu](https://github.com/arisuland/Arisu)|Frontend UI of Arisu, made with React|![status](https://img.shields.io/github/workflow/status/arisuland/Arisu/ESLint/master?style=flat-square)|
|🖋️ [bell](https://github.com/arisuland/bell)|Automation bot for GitHub to push translations into Arisu or vice versa.||![status](https://img.shields.io/github/workflow/status/arisuland/Tsubaki/Build/master?style=flat-square)|
|⛴ [cli](https://github.com/arisuland/cli)|A command-line interface to automate the process of handling translations, merging translations, etc.|![status](https://img.shields.io/github/workflow/status/arisuland/Build/ktlint/master?style=flat-square)|
|🐳 [docs](https://github.com/arisuland/docs)|Documentation site for Arisu, showcasing the REST API and other stuff|![status](https://img.shields.io/github/workflow/status/arisuland/docs/Production/master?style=flat-square)|
|🥀 [tsubaki](https://github.com/arisuland/Tsubaki)|Backend infrastructure for Arisu, all the magic begins here~ (You're here!)|![status](https://img.shields.io/github/workflow/status/arisuland/tsubaki/ktlint/master?style=flat-square)|
|📃 [translations](https://github.com/arisuland/translations)|Translation monorepo for Arisu|![status](https://img.shields.io/github/workflow/status/arisuland/translations/push/master?style=flat-square)|
|📜 [themes](https://github.com/arisuland/themes)|Customizable themes for Arisu, so style however you want to!|![status](https://img.shields.io/github/workflow/status/arisuland/themes/stylelint/master?style=flat-square)|

## Projects
- [bootstrap](https://github.com/arisuland/Tsubaki/tree/master/bootstrap) **~** Application to bootstrap Tsubaki to the world!
- [common](https://github.com/arisuland/Tsubaki/tree/master/common) **~** Common utilities for Tsubaki.
- [core](https://github.com/arisuland/Tsubaki/tree/master/core) **~** Core foundation of Tsubaki.
- [storage](https://github.com/arisuland/Tsubaki/tree/master/storage) **~** Storage providers for holding project files.

## Installation
Before running Tsubaki, you must need the following tools before we begin:

- [**PostgresSQL** v11+](https://postgresql.org) **~** Main database for Tsubaki
- [**Redis** v6.2+](https://redis.io) **~** In-memory data store for Tsubaki
- [**JDK** 11+](https://adoptopenjdk.net/) **~** Runtime engine for Tsubaki
- [**Git** v2.31+](https://git-scm.com) **~** Version control software to pull updates from.

There are other tooling that Tsubaki has support, that you can use:

- [**Docker**](https://www.docker.com) ~ A containerization tool that allows you to run Tsubaki on any platform.
- [**Sentry**](https://sentry.io) ~ A monitoring tool for Tsubaki.

### Installation
You wish to use Docker, I recommend reading the [Docker](#installation-docker) section below.

```sh
# 1. Clone the repository from GitHub
$ git clone https://github.com/arisuland/Tsubaki.git && cd Tsubaki

# 2. Installs all dependencies in all subprojects and creates a `.jar` executable
$ ./gradlew :bootstrap:shadowJar

# 3. Run Tsubaki! (omit `$BRANCH` with the branch you cloned from, e.g. `master`)
$ java -jar build/libs/Tsubaki-$BRANCH.jar
```

> Open a new browser session and go to `http://localhost:23098/` to see Tsubaki in action.

### Installation (Docker)
You can run Tsubaki with Docker, but you will need to have Docker installed on your machine before getting started.

|Branch|Image|
|------|------|
|`master`|`arisuland/tsubaki:latest`|
|`staging`|`arisuland/tsubaki:staging`|

```sh
# 1. Pull the image from Docker
$ docker pull arisuland/tsubaki:<branch>

# 2. Run the image
$ docker run -d -p 9999:23098 arisuland/arisu:<branch> /
  --volume './config.yml:/opt/Tsubaki/config.yml' /
  --name 'tsubaki' /
  arisuland/tsubaki:<branch>
```

> Open a new browser session and go to `http://localhost:9999/` to see Tsubaki in action.

## Configuration
Tsubaki is configured via a `.yml` file, which is located in the root of the project.

```yml
# Runs all pending migrations, if any
runPendingMigrations: true

# The environment when running Tsubaki,
# this is always overrided if `TSUBAKI_ENV` is provided
# as an environment label.
environment: 'development'

# DSN for connecting to Sentry, if using Sentry.
sentryDSN: ''

# The hostname to connect Tsubaki to the world, e.g. "localhost"
host: ''

# Configuration for the database
database:
  database: '' # The name of the database to connect to
  username: '' # The username to connect to the database with
  password: '' # Password for the database user
  host: ''     # The hostname of the database.
  port: 5432   # The port of the database.
  url: ''      # The url to connect to the database.

# Configuration for the Redis instance
redis:
  sentinels: # A list of sentinels to connect to (see https://redis.io/topics/sentinel for more information)
    - array of
    - host:port

  password: '' # Password for the Redis instance
  master: ''   # The name of the master to connect to
  port: 6379   # The port of the Redis instance
  host: ''     # The hostname of the Redis instance
  db: 0        # The database to connect to

# Configuration for the ratelimiter
ratelimiting:
  cacheStyle: "in-memory" # Cache ratelimiting objects only in-memory, or in Redis, or `both` to do both.
  maxRequests: 0          # The maximum number of requests per minute
  maxTime: 10             # The maximum time in seconds to ratelimit
  
# Configuration for in-memory cache
caching:
  memory:
    # The duration limit "as" property
    asAge: days
    
    # A duration on expiring old cache (in days or a specified `asAge` property)
    maxAge: 7
    
    # Returns the maximum amount of items to cache
    maxItems: 100000

# Configuration for Netty
# For more information, check out the Ktor api docs:
# https://api.ktor.io/ktor-server/ktor-server-netty/ktor-server-netty/io.ktor.server.netty/-netty-application-engine/-configuration/index.html
netty:
  # Timeout in seconds for sending responses to client
  responseWriteTimeoutSeconds: 10

  # Timeout in seconds for reading requests from client, "0" is infinite.
  requestReadTimeoutSeconds: 0

  # Size of the queue to store requests that cannot be immediately processed.
  requestQueueLimit: 16

  # Do not create seperate call event groups and reuse worker group for processing calls
  shareWorkGroup: false

  # Enables TCP keep alive for connections.
  tcpKeepAlive: false

  # Number of concurrently running requests from the same HTTP pipeline
  runningLimit: 10
```

## Storage Configuration
Tsubaki has a feature called **storage trailers**, where you can store all your projects
into a GCS/S3 bucket, or in the filesystem! If you would like to create a storage trailer
for support, read the [readme](./storage/README.md) for more information on how to.

### S3
The S3 storage trailer supports **Amazon S3** and **Wasabi** (Tsubaki uses this storage trailer with Wasabi in production!),
if you wish to use this, the configuration bit is like:

```yml
# You cannot have multiple storage trailers
storage:
  # Define the S3 option
  s3:
    # If you wish to use Wasabi instead of Amazon S3
    use_wasabi: true
    
    # The access key to use, if this is null,
    # it'll use where you saved it at
    access_key: "..."

    # The secret key to use, if this is null,
    # it'll use where you saved it at
    secret_key: "..."
    
    # The bucket to use, if the bucket wasn't
    # found, it'll create it when you run Tsubaki
    bucket: "tsubaki"
```

### Google Cloud Storage
Tsubaki supports GCS as another option in-case you're against AWS S3 or whatever. Before you use this storage trailer,
I recommend setting up first and [adding authentication](https://cloud.google.com/storage/docs/reference/libraries#setting_up_authentication).

```yml
# You cannot have multiple storage trailers
storage:
  gcs:
    # The bucket to use, if the bucket wasn't
    # found, it'll create it when you run Tsubaki.
    bucket: "tsubaki"
```

### Filesystem
Tsubaki supports the filesystem as another option for testing, this is also used for unit-testing.

```yml
# You cannot have multiple storage trailers
storage:
  filesystem:
    # The directory to use, if the directory
    # wasn't found, it'll create it
    directory: "./data/tsubaki"
```

If you plan to use Docker with the filesystem storage trailer, you must add a volume:

```shell
$ docker run -v <some-path>/data/tsubaki:/opt/Tsubaki/data/tsubaki ...
```

#### Example with Docker Compose
```yml
services:
  tsubaki:
    volumes:
      - tsubaki_projects:/opt/Tsubaki/data/tsubaki

volumes:
  tsubaki_projects:
```

## License
**Tsubaki** is released under the **GPL-3.0** License. :3
