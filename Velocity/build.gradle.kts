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
    id("java")
}

repositories {
    mavenCentral()
    maven("https://repo.minebench.de/")
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:1.1.3-SNAPSHOT")
    compile("de.themoep:minedown-adventure:1.7.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:1.1.3-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        dependencies {
            include(dependency("de.themoep:minedown-adventure"))
        }

        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.velocity.minedown")
    }
}