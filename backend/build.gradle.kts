plugins {
    java
    jacoco
    id("io.freefair.lombok") version "8.3"
    id("org.springframework.boot") version "3.1.3"
    id("io.spring.dependency-management") version "1.1.3"
    id("org.sonarqube") version "4.4.1.3373"
}

sonar {
  properties {
    property("sonar.projectKey", "safe-zone-backend")
    property("sonar.projectName", "safe-zone-backend")
  }
}

allprojects {
    apply {
        plugin("java")
        plugin("jacoco")
        plugin("io.freefair.lombok")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }

    group = "pw.mer"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    // always generate jacoco coverage report after running tests
    tasks.test {
        finalizedBy(tasks.jacocoTestReport)
    }
    tasks.jacocoTestReport {
        dependsOn(tasks.test)
        reports {
            xml.required = true
        }
    }
}