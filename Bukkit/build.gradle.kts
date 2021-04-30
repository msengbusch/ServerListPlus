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
    id("net.minecrell.plugin-yml.bukkit") version "0.3.0"
}

repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.minecrell.net/snapshots/")
    maven("https://destroystokyo.com/repo/repository/maven-snapshots/")
    maven("https://repo.dmulloy2.net/nexus/repository/releases/")
    maven("https://repo.dmulloy2.net/nexus/repository/snapshots/")
    maven("https://ci.frostcast.net/plugin/repository/everything/")
    maven("https://jitpack.io/")
}

dependencies {
    compileOnly(libs.paper)
    compileOnly(libs.protocollib) { isTransitive = false }
    compileOnly(libs.banmanager) { isTransitive = false }
    compileOnly(libs.ipaddress) /* For BanManager */
    compileOnly(libs.maxbans) { isTransitive = false }
    compileOnly(libs.lombok)

    implementation(libs.metricslite) { isTransitive = false }
    implementation(libs.minedown.core)

    annotationProcessor(libs.lombok)
}

bukkit {
    apiVersion = "1.13"
    main = "net.minecrell.serverlistplus.bukkit.BukkitPlugin"

    name = rootProject.name
    softDepend = listOf("ProtocolLib", "AdvancedBan", "BanManager", "MaxBans")

    commands {
        create("serverlistplus") {
            description = "Configure ServerListPlus"
            // I have no idea why I added so many weird aliases back then... "slp" is the only relevant one
            aliases = listOf("slp", "serverlist+", "serverlist", "sl+", "s++", "serverping+", "serverping", "spp", "slus")
        }
    }

    permissions {
        create("serverlistplus.admin") {
            description = "Allows you to access the ServerListPlus administration commands"
        }
    }
}

tasks {
    withType<ShadowJar> {
        relocate("org.mcstats", "net.minecrell.serverlistplus.bukkit.mcstats")
        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.bukkit.minedown")

        dependencies {
            include(dependency(libs.metricslite.get()))
            include(dependency(libs.minedown.core.get()))
        }
    }

    // Remapped artifacts for compatibility with 1.7.x and 1.8
    fun createShadowTask(name: String, configure: ShadowJar.() -> Unit) {
        create<ShadowJar>("shadow$name") {
            classifier = "${project.name}-$name"
            configurations = listOf(project.configurations["runtimeClasspath"])
            from(sourceSets["main"].output)
            configure()
        }
    }

    createShadowTask("1.7.X") {
        relocate("com.google.common", "net.minecraft.util.com.google.common")
        relocate("com.google.gson", "net.minecraft.util.com.google.gson")
    }

    createShadowTask("1.8") {
        relocate("com.google.gson", "org.bukkit.craftbukkit.libs.com.google.gson")
    }
}
