plugins {
    kotlin("jvm") version "1.9.23"
}

group = "com.zenmo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.apache.poi:poi:5.2.5")
    implementation("org.apache.poi:poi-ooxml:5.2.5")

//    implementation("org.apache.logging.log4j:log4j-core:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-api:2.14.1")
//    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.1")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.zenmo.MainKt"
    }
    from (
        configurations
            .runtimeClasspath
            .get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    ) {
        exclude("META-INF/versions/9/module-info.class")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

tasks.register<JavaExec>("run") {
    group = "application"
    description = "Runs the Kotlin application."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.zenmo.MainKt")
}

kotlin {
    jvmToolchain(21)
}