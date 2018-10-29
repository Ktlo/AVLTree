import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.2.71"
    application
}

group = "com.handtruth.avltree"
version = "1.0"

repositories {
    mavenCentral()
}

val junitVer = "5.3.1"

dependencies {
    compile(kotlin("stdlib-jdk8"))
    compile(group = "no.tornado", name = "tornadofx", version = "1.7.17")

    testCompile(group = "org.jetbrains.kotlin", name = "kotlin-test")
    testCompile(group = "org.junit.jupiter", name = "junit-jupiter-api", version = junitVer)
    testCompile(group = "org.junit.jupiter", name = "junit-jupiter-params", version = junitVer)
    testRuntime(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = junitVer)
}

application {
    mainClassName = "com.handtruth.avltree.presentation.PresentationApp"
}

tasks {
    withType<Test> {
        useJUnitPlatform()
    }

    withType<Jar> {
        manifest {
            attributes(mapOf("Implementation-Title" to project.name,
                             "Implementation-Version" to version,
                             "Main-Class" to application.mainClassName))
        }
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }
}