plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.testng:testng:7.10.0")
    testImplementation("org.slf4j:slf4j-simple:2.0.7")

    implementation("org.seleniumhq.selenium:selenium-java:4.24.0")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
}

tasks.test {
    useTestNG()
}