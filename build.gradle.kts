plugins {
    id("java")
}

group = "com.smolagents4j"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(platform("org.springframework.ai:spring-ai-bom:1.0.0"))
    implementation("org.springframework.ai:spring-ai-huggingface")
    implementation("org.springframework.ai:spring-ai-openai")

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
