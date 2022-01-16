/**
 * 🥑 datetime-scalars: Adds scalar support for kotlinx.datetime for GraphQL Java
 * Copyright (C) 2021 Noelware
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

import java.text.SimpleDateFormat
import java.util.Properties
import java.util.Date

plugins {
    id("com.diffplug.spotless") version "5.14.0"
    id("org.jetbrains.dokka") version "1.4.30"
    kotlin("jvm") version "1.5.10"
    id("io.kotest") version "0.3.8"
    `maven-publish`
}

group = "land.arisu.libraries"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    // Kotlin libraries
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.2.1")
    implementation(kotlin("stdlib", "1.5.10"))

    // GraphQL
    implementation("com.graphql-java:graphql-java:16.2")

    // Testing libraries
    testImplementation("io.kotest:kotest-assertions-core-jvm:4.6.0")
    testImplementation("io.kotest:kotest-runner-junit5-jvm:4.6.0")
    testImplementation("io.kotest:kotest-property-jvm:4.6.0")
}

spotless {
    kotlin {
        trimTrailingWhitespace()
        licenseHeaderFile("${rootProject.projectDir}/assets/HEADER")
        endWithNewline()

        // We can't use the .editorconfig file, so we'll have to specify it here
        // issue: https://github.com/diffplug/spotless/issues/142
        ktlint()
            .userData(mapOf(
                "no-consecutive-blank-lines" to "true",
                "no-unit-return" to "true",
                "disabled_rules" to "no-wildcard-imports,colon-spacing",
                "indent_size" to "4"
            ))
    }
}

tasks.register("generateMetadata") {
    val path = sourceSets["main"].resources.srcDirs.first()
    if (!file(path).exists()) path.mkdirs()

    val date = Date()
    val formatter = SimpleDateFormat("MMM dd, yyyy @ hh:mm:ss")

    file("$path/metadata.properties").writeText("""built.at = ${formatter.format(date)}
app.version = $version
""".trimIndent())
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        kotlinOptions.javaParameters = true
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    dokkaHtml {
        outputDirectory.set(file("${rootProject.projectDir}/docs"))

        dokkaSourceSets {
            configureEach {
                platform.set(org.jetbrains.dokka.Platform.jvm)
                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(uri("https://github.com/arisuland/datetime-scalars/tree/master/src/main/kotlin").toURL())
                    remoteLineSuffix.set("#L")
                }

                jdkVersion.set(11)
            }
        }
    }
}


val publishingProps = try {
    Properties().apply { load(file("${rootProject.projectDir}/gradle/publishing.properties").reader()) }
} catch(e: Exception) {
    Properties()
}

val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.main.get().allSource)
}

val dokkaJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assemble Kotlin documentation with Dokka"

    archiveClassifier.set("javadoc")
    from(tasks.dokkaHtml)
    dependsOn(tasks.dokkaHtml)
}

publishing {
    publications {
        create<MavenPublication>("Haru") {
            from(components["kotlin"])
            groupId = "land.arisu.libraries"
            artifactId = "datetime-scalars"
            version = "1.0.0"

            artifact(sourcesJar.get())
            artifact(dokkaJar.get())

            pom {
                description.set("Adds scalar support for kotlinx.datetime for GraphQL Java")
                name.set("datetime-scalars")
                url.set("https://arisuland.github.io/datetime-scalars")

                organization {
                    name.set("Arisu")
                    url.set("https://arisu.land")
                }

                developers {
                    developer {
                        email.set("cutie@floofy.dev")
                        name.set("Noel")
                    }
                }

                issueManagement {
                    system.set("GitHub")
                    url.set("https://github.com/arisuland/datetime-scalars/issues")
                }

                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }

                scm {
                    connection.set("scm:git:ssh://github.com/arisuland/datetime-scalars.git")
                    developerConnection.set("scm:git:ssh://git@github.com:arisuland/datetime-scalars.git")
                    url.set("https://github.com/arisuland/datetime-scalars")
                }
            }
        }
    }

    repositories {
        maven(url = "s3://maven.arisu.land/repo/releases") {
            credentials(AwsCredentials::class.java) {
                accessKey = publishingProps.getProperty("s3.accessKey") ?: ""
                secretKey = publishingProps.getProperty("s3.secretKey") ?: ""
            }
        }
    }
}
