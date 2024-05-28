plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "guru.ysy"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://repo.spring.io/milestone") }
}

extra["springAiVersion"] = "0.8.1"
extra["openAPIVersion"] = "2.5.0"
extra["tikaVersion"] = "2.9.2"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.ai:spring-ai-pgvector-store-spring-boot-starter")
    implementation("org.springframework.ai:spring-ai-mistral-ai-spring-boot-starter")

    // jdbc
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    // postgresql
    implementation("org.postgresql:postgresql")
    // OpenAPI
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:${property("openAPIVersion")}")

    // Tika document reader
    implementation("org.apache.tika:tika-core:${property("tikaVersion")}")
    implementation("org.apache.tika:tika-parsers-standard-package:${property("tikaVersion")}")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // lombok for test
    testCompileOnly("org.projectlombok:lombok")
    testAnnotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // explicit declare JUnit5 for testing ready for gradle 9
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")

        showExceptions = true
        setExceptionFormat("full")
        showCauses = true
        showStackTraces = true

        showStandardStreams = true // show more verbose test output
    }
    // avoid dynamic agent loading warning for JDK21
    jvmArgs("-XX:+EnableDynamicAgentLoading")
}
