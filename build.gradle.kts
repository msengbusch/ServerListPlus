/*
 * ServerListPlus - https://git.io/slp
 * Copyright (C) 2014 Minecrell (https://github.com/Minecrell)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    `maven-publish`

    id("com.github.johnrengelman.shadow") version "6.1.0" apply false
    id("org.cadixdev.licenser") version "0.5.1"
    id("com.github.ben-manes.versions") version "0.38.0"
}

allprojects {
    plugins.apply("java")
    plugins.apply("org.cadixdev.licenser")

    repositories {
        mavenCentral()

        maven("https://repo.minebench.de/")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    license {
        header = rootProject.file("src/main/resources/LICENSE")
        include("**/*.java")
        include("**/*.kts")

        tasks {
            create("gradle") {
                files = project.files("build.gradle.kts", "settings.gradle.kts")
            }
        }
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.isDeprecation = true
    }
}

subprojects {
    plugins.apply("com.github.johnrengelman.shadow")

    base {
        archivesBaseName = "${rootProject.name}$archivesBaseName"
    }

    dependencies {
        implementation(rootProject)
    }

    tasks.withType<ShadowJar> {
        artifacts.add("archives", this)

        archiveBaseName.set(rootProject.name)
        classifier = project.name

        exclude("META-INF/")

        dependencies {
            include(project(rootProject.path))
            include(dependency("org.ocpsoft.prettytime:prettytime"))
        }

        relocate("org.ocpsoft.prettytime", "net.minecrell.serverlistplus.core.lib.prettytime")
    }
}

defaultTasks("clean", "build")

repositories {
    maven("https://jitpack.io/")
}

dependencies {
    implementation(libs.guava)
    implementation(libs.snakeyaml)
    implementation(libs.gson)
    implementation(libs.prettytime)

    compileOnly(libs.lombok)
    compileOnly(libs.slf4j)
    compileOnly(libs.advancedban) { isTransitive = false }

    testImplementation(libs.junit)
    testImplementation(libs.mockito)

    annotationProcessor(libs.lombok)
}

tasks {
    // Copy project properties, loaded at runtime for version information
    getByName<Copy>("processResources") {
        expand(project.properties) // Replace variables in HEADER file

        from("gradle.properties") {
            into("net/minecrell/serverlistplus/core")
        }
    }

    // Universal JAR that works on multiple platforms
    create<Jar>("universal") {
        artifacts.add("archives", this)

        classifier = "Universal"
        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)

        for (p in arrayOf("Bukkit", "Bungee", "Sponge", "Velocity")) {
            val task = project(p).tasks["shadowJar"]
            dependsOn(task)
            from(zipTree(task.outputs.files.singleFile))
        }
    }

    // Bundle all sources together into one source JAR
    create<Jar>("sourceJar") {
        artifacts.add("archives", this)

        classifier = "sources"
        allprojects {
            from(sourceSets["main"].allSource)
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks["universal"])
            artifact(tasks["sourceJar"])

            subprojects {
                tasks.withType<ShadowJar> {
                    artifact(this)
                }
            }
        }
    }

    repositories {
        val mavenUrl: String? by project
        val mavenSnapshotUrl: String? by project

        (if (version.toString().endsWith("-SNAPSHOT")) mavenSnapshotUrl else mavenUrl)?.let { url ->
            maven(url) {
                val mavenUsername: String? by project
                val mavenPassword: String? by project
                if (mavenUsername != null && mavenPassword != null) {
                    credentials {
                        username = mavenUsername
                        password = mavenPassword
                    }
                }
            }
        }
    }
}