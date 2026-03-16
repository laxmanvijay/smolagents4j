plugins {
    id("java")
    id("org.springframework.boot") version("3.4.3")
    id("io.spring.dependency-management") version("1.1.4")
}

group = "com.smolagents4j"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.4.3"))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0"))
    implementation("org.springframework.ai:spring-ai-starter-model-huggingface")

    implementation("org.postgresql:postgresql:42.7.4")
    implementation("org.springframework.data:spring-data-jpa:3.2.5")
    implementation("org.hibernate.orm:hibernate-core:6.4.4.Final")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
