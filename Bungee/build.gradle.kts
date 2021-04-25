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
    id("net.minecrell.plugin-yml.bungee") version "0.3.0"
    id("java")
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.minecrell.net/releases/")
    maven("https://repo.minebench.de/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.16-R0.4-SNAPSHOT")

    compileOnly("com.github.lucavinci:bungeeban:v2.7.0") { isTransitive = false }

    implementation("net.minecrell.mcstats:statslite-bungee:0.2.3")
    implementation("de.themoep:minedown:1.7.0-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.20")
    annotationProcessor("org.projectlombok:lombok:1.18.20")
}

bungee {
    main = "net.minecrell.serverlistplus.bungee.BungeePlugin"

    name = rootProject.name
    softDepends = setOf("AdvancedBan", "BungeeBan")
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        dependencies {
            include(dependency("net.minecrell.mcstats:statslite-bungee"))
            include(dependency("de.themoep:minedown"))
        }

        relocate("net.minecrell.mcstats", "net.minecrell.serverlistplus.mcstats")
        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.bungee.minedown")
    }
}
