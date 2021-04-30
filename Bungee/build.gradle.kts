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
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.minecrell.net/releases/")
    maven("https://jitpack.io/")
}

dependencies {
    implementation(libs.statslite.bungee)
    implementation(libs.minedown.core)

    compileOnly(libs.bungeecord)
    compileOnly(libs.bungeeban) { isTransitive = false }
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
}

bungee {
    main = "net.minecrell.serverlistplus.bungee.BungeePlugin"

    name = rootProject.name
    softDepends = setOf("AdvancedBan", "BungeeBan")
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        relocate("net.minecrell.mcstats", "net.minecrell.serverlistplus.mcstats")
        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.bungee.minedown")

        dependencies {
            include(dependency(libs.statslite.bungee.get()))
            include(dependency(libs.minedown.core.get()))
        }
    }
}
