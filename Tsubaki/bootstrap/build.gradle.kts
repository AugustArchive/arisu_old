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

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import land.arisu.tsubaki.gradle.execShell

plugins {
    id("com.github.johnrengelman.shadow") version Versions.SHADOW
    application
}

dependencies {
    // Projects
    implementation(project(":common"))
    implementation(project(":core"))

    // Logging (so logback can work:tm:)
    implementation(logbackAPI)
    api(slf4j("api"))
    implementation(logback)
}

application {
    mainClass.set("land.arisu.tsubaki.bootstrap.Bootstrap")
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

tasks {
    named<ShadowJar>("shadowJar") {
        val branch = execShell("git branch --show-current")

        archiveFileName.set("Tsubaki-$branch.jar")
        mergeServiceFiles()
        manifest {
            attributes(mapOf(
                "Manifest-Version" to "1.0.0",
                "Main-Class" to "land.arisu.tsubaki.bootstrap.Bootstrap"
            ))
        }
    }

    build {
        dependsOn(spotlessApply)
        dependsOn(shadowJar)
    }
}
