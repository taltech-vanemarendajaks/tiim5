plugins {
    java
    jacoco
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
    alias(libs.plugins.spotless)
    id("org.sonarqube") version "7.2.3.7755"
}

group = "com.tiim5"
version = "0.0.1-SNAPSHOT"
description = "StudyPlanner application"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(25)
	}
}

repositories {
	mavenCentral()
}

dependencies {
    implementation(libs.spring.boot.starter.webmvc) 
    implementation(libs.springdoc.webmvc.ui)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.liquibase)

    developmentOnly(libs.spring.boot.devtools)

    testImplementation(libs.spring.boot.starter.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)

    runtimeOnly(libs.postgresql)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

spotless {
    java {
        googleJavaFormat()
        target("src/**/*.java")
    }
}

tasks.named("compileJava") {
    dependsOn("spotlessApply")
}

jacoco {
    toolVersion = "0.8.14"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
    }
}

sonar {
    properties {
        property("sonar.projectKey", "tiim-5_studyplanner-backend")
        property("sonar.organization", "tiim-5")
        property("sonar.projectBaseDir", rootProject.projectDir.absolutePath)
        property("sonar.sources", "src/main/java")
        property("sonar.java.coveragePlugin", "jacoco")
        property(
            "sonar.coverage.jacoco.xmlReportPaths",
            "${layout.buildDirectory.get()}/reports/jacoco/test/jacocoTestReport.xml"
        )
        property("sonar.exclusions", "**/config/**, **/entity/**, **/*StudyPlanner*")
    }
}
