/*
 * Copyright (c) 2023.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    // https://plugins.gradle.org/plugin/org.springframework.boot
    id("org.springframework.boot") version "3.1.2"

    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management") version "1.1.3"

    // https://github.com/graalvm/native-build-tools
    id("org.graalvm.buildtools.native") version "0.9.24"

    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.jvm
    kotlin("jvm") version "1.9.0"
    // https://plugins.gradle.org/plugin/org.jetbrains.kotlin.plugin.spring
    kotlin("plugin.spring") version "1.9.0"
}

group = "dev.mbo"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    implementation("org.springframework.boot:spring-boot-starter-mail")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    // https://mvnrepository.com/artifact/io.netty/netty-resolver-dns-native-macos
    implementation("io.netty:netty-resolver-dns-native-macos:4.1.96.Final:osx-aarch_64")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform()
    }

    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    bootJar {
        archiveFileName = "${project.name}-all.jar"
        exclude("application-sec*.yml")
    }

    withType<Wrapper> {
        // https://gradle.org/releases/
        gradleVersion = "8.2.1"
        distributionType = Wrapper.DistributionType.ALL
    }

    graalvmNative {
        binaries {
            named("main") {
                mainClass.set("dev.mbo.linkshortener2.ApplicationKt")
            }
        }
    }

}