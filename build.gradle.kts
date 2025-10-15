plugins {
	java
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "org.example"
version = "0.0.1-SNAPSHOT"
description = "Training project for Spring Boot and kafka"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation ("org.springframework.kafka:spring-kafka")
    implementation ("org.springframework.boot:spring-boot-starter-mail")

    implementation("org.postgresql:postgresql")

    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    implementation("org.mapstruct:mapstruct:1.6.3")
    testAnnotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    implementation("net.datafaker:datafaker:2.5.2")
    implementation("org.instancio:instancio-junit:5.5.1")

    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.21.3")
    testImplementation("org.testcontainers:postgresql:1.21.3")

    testImplementation ("org.springframework.kafka:spring-kafka-test")
    testImplementation ("org.testcontainers:kafka:1.21.3")

    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:4.1.1")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
    systemProperty("testcontainers.reuse.enable", "true")
}
