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
    id("org.spongepowered.gradle.plugin") version "1.0.1"
}

repositories {
    maven("https://repo.minecrell.net/releases/")
}

dependencies {
    compileOnly(libs.sponge)
    annotationProcessor(libs.sponge)

    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)

    compileOnly(libs.statusprotocol)
    implementation(libs.statslite.sponge)
}

sponge {
    plugin("serverlistplus") {
        description("A flexible Minecraft plugin to customize the appearance of your server in the server list")
        mainClass("net.minecrell.serverlistplus.sponge.SpongePlugin")
        loader(org.spongepowered.gradle.plugin.config.PluginLoaders.JAVA_PLAIN)
        links {
            homepage("https://ci.minebench.de/job/ServerListPlus/")
            source("https://github.com/Minebench/ServerListPlus")
            issues("https://github.com/Minebench/ServerListPlus/issues")
        }
    }
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        dependencies {
            include(dependency(libs.statslite.sponge.get()))
        }

        relocate("net.minecrell.mcstats", "net.minecrell.serverlistplus.mcstats")
    }
}
