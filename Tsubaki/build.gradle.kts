/**
 * ☔🥀 Tsubaki: Core backend infrastructure for Arisu, all the magic begins here ✨🚀
 * Copyright (C) 2020-2021 Noelware
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

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import land.arisu.tsubaki.gradle.Version

buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }

    dependencies {
        classpath("com.diffplug.spotless:spotless-plugin-gradle:${Versions.SPOTLESS}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.KOTLIN}")
        classpath("io.kotest:kotest-gradle-plugin:${Versions.KOTEST_GRADLE}")
    }
}

plugins {
    id("com.diffplug.spotless") version Versions.SPOTLESS
    kotlin("jvm") version Versions.KOTLIN
    application
}

repositories {
    mavenCentral()
    mavenLocal()
    jcenter() // todo: remove this when kotlinx decides to not be a :woeme: moment
}

val current = Version(1, 0, 0)
group = "land.arisu"
version = current.string()

subprojects {
    group = "land.arisu.tsubaki"
    version = current.string()

    apply(plugin = "kotlin")
    apply(plugin = "kotlinx-serialization")
    apply(plugin = "com.diffplug.spotless")

    // Check if projects are testable
    val isTestable = when (this.name) {
        "common", "middleware", "core" -> true
        else -> false
    }

    if (isTestable) {
        apply(plugin = "io.kotest")
    }

    repositories {
        mavenCentral()
        mavenLocal()
        jcenter()

        maven {
            url = uri("https://maven.floofy.dev/repo/releases")
        }
    }

    dependencies {
        // Add important Kotlin libraries
        implementation("org.jetbrains.kotlin:kotlin-stdlib")
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
        api("org.jetbrains.kotlinx:kotlinx-serialization-core:1.2.2")

        if (isTestable) {
            testImplementation(kotest("assertions-core-jvm"))
            testImplementation(kotest("runner-junit5-jvm"))
            testImplementation(kotest("property-jvm"))
        }
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

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        kotlinOptions.javaParameters = true
        kotlinOptions.freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn"
        )
    }

    if (isTestable) {
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }
}
