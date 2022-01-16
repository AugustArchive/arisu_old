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

dependencies {
    // Tsubaki
    implementation(project(":storage"))
    implementation(project(":common"))

    // Koin (Dependency Injection)
    implementation(koin("ktor"))
    implementation(koin("core-ext"))
    implementation(koin("logger-slf4j"))

    // Logging
    implementation(slf4j("simple"))
    implementation(logback)
    api(slf4j("api"))

    // Ktor (HTTP Server)
    implementation(ktor("client-serialization"))
    implementation(ktor("client-okhttp"))
    implementation(ktor("serialization"))
    implementation(ktor("server-netty"))
    implementation(ktor("server-core"))

    // Cache (Caffeine & Redis)
    implementation(coffee)
    implementation(lettuce)

    // Database
    implementation(exposed("dao", "0.32.1"))
    implementation(exposed("jdbc", "0.32.1"))
    implementation(postgresql)
    implementation(exposed())
    implementation(hikari)

    // Scheduling
    implementation(haru)

    // Hashing
    implementation(apacheCommonsCodec)

    // Configuration
    implementation(kaml)

    // Argon2 implementation (for passwords)
    implementation(argon2)

    // Metrics (Prometheus)
    implementation(ktor("metrics-micrometer"))
    implementation(micrometer)

    // JWT authentication
    implementation(jjwt("gson"))
    implementation(jjwt("impl"))
    api(jjwt("api"))

    // Sentry
    implementation(sentry)
}
