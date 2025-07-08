import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Property
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile
import org.jooq.meta.jaxb.Generate
import org.jooq.meta.jaxb.Matchers
import org.jooq.meta.jaxb.MatchersTableType
import org.jooq.meta.jaxb.Strategy

plugins {
    kotlin("jvm") version "2.2.0"
    kotlin("plugin.spring") version "2.2.0"
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("nu.studer.jooq") version "8.2.3"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2025.0.0"

val jooqVersion = "3.20.5"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.8")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign") {
        exclude(group = "javax.servlet")
    }
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Kotlin
    implementation(platform("org.jetbrains.kotlin:kotlin-bom:2.0.21"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Logging
    implementation("io.github.oshai:kotlin-logging-jvm:7.0.7")
    implementation("ch.qos.logback:logback-classic:1.5.18")

    // Db
    implementation("org.liquibase:liquibase-core:4.32.0")
    runtimeOnly("org.postgresql:postgresql:42.7.7")

    // Security
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("io.github.openfeign:feign-okhttp")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Jooq
    implementation("org.jooq:jooq:$jooqVersion")
    jooqGenerator("org.jooq:jooq-meta-extensions:$jooqVersion")
}

jooq {
    version.set(jooqVersion)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                generator.apply {
                    name = "org.jooq.codegen.KotlinGenerator"

                    generate = Generate()
                        .withPojos(true)
                        .withDaos(true)
                        .withSpringAnnotations(true)
                        .withSpringDao(true)
                        .withJavaTimeTypes(true)
//                    TODO добавить флаг на проверку null

                    database = org.jooq.meta.jaxb.Database().apply {
                        name = "org.jooq.meta.extensions.ddl.DDLDatabase"
                        properties = listOf(
                            Property().withKey("scripts").withValue("src/main/resources/db/changelog/schema/*.sql"),
                            Property().withKey("sort").withValue("alphanumeric"),
                            Property().withKey("defaultNameCase").withValue("lower"),
// TODO: добавить профили градла для жука и ликвы
//                            перенести генерацию в src
                            Property().withKey("parseIgnoreComments").withValue("true"),
                            Property().withKey("parseIgnoreCommentStart").withValue("[jooq ignore start]"),
                            Property().withKey("parseIgnoreCommentStop").withValue("[jooq ignore stop]")
                        )
                    }

                    target = org.jooq.meta.jaxb.Target().apply {
                        packageName = "com.example.jooq.generated"
                        directory = "$buildDir/generated-src/jooq/main"
                    }

                }
            }
        }
    }
}

tasks.withType<KotlinJvmCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
    }
}