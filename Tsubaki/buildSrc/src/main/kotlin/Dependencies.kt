/**
 * Tsubaki: Core backend infrastructure for Arisu, all the magic begins here ✨🚀
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

fun kotlinx(module: String, version: String? = Versions.KOTLIN): String
        = "org.jetbrains.kotlinx:kotlinx-$module:$version"

fun koin(module: String): String
        = "io.insert-koin:koin-$module:${Versions.KOIN}"

fun slf4j(module: String): String
        = "org.slf4j:slf4j-$module:${Versions.SLF4J}"

fun ktor(module: String): String
        = "io.ktor:ktor-$module:${Versions.KTOR}"

fun kotest(module: String): String
        = "io.kotest:kotest-$module:${Versions.KOTEST}"

fun exposed(module: String = "", version: String = Versions.EXPOSED): String
        = "org.jetbrains.exposed:exposed${if (module != "") "-$module" else ""}:$version"

fun jjwt(module: String): String
        = "io.jsonwebtoken:jjwt-$module:${Versions.JJWT}"

const val hikari = "com.zaxxer:HikariCP:${Versions.HIKARI}"
const val postgresql = "org.postgresql:postgresql:${Versions.POSTGRES}"
const val kaml = "com.charleskorn.kaml:kaml:${Versions.KAML}"
const val logback = "ch.qos.logback:logback-classic:${Versions.LOGBACK}"
const val gcs = "com.google.cloud:google-cloud-storage"
const val aws = "software.amazon.awssdk:s3:${Versions.AWS}"
const val lettuce = "io.lettuce:lettuce-core:${Versions.LETTUCE_FOR_MY_SANDWICH}"
const val coffee = "com.github.ben-manes.caffeine:caffeine:${Versions.COFFEE}"
const val logbackAPI = "ch.qos.logback:logback-core:${Versions.LOGBACK}"
const val haru = "dev.floofy.haru:Haru:${Versions.HARU}"
const val gcsBom = "com.google.cloud:libraries-bom:${Versions.GCS_BOM}"
const val apacheCommonsCodec = "commons-codec:commons-codec:${Versions.COMMONS_CODEC}"
const val argon2 = "de.mkammerer:argon2-jvm:${Versions.ARGON2}"
const val classpath = "io.github.classgraph:classgraph:${Versions.CLASSPATH}"
const val micrometer = "io.micrometer:micrometer-registry-prometheus:${Versions.MICROMETER}"
const val sentry = "io.sentry:sentry:${Versions.SENTRY}"
