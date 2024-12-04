plugins {
    kotlin("jvm") version "2.1.0"
}

kotlin {
    jvmToolchain(23)
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
    test {
        kotlin.srcDir("test")
    }
}

tasks {
    wrapper {
        gradleVersion = "8.11.1"
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}