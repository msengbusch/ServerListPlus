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

repositories {
    maven("https://repo.minebench.de/")
    maven("https://repo.velocitypowered.com/snapshots/")
}

dependencies {
    implementation(libs.minedown.adventure)

    compileOnly(libs.velocity)
    compileOnly(libs.lombok)

    annotationProcessor(libs.velocity)
    annotationProcessor(libs.lombok)
}

tasks {
    getByName<ShadowJar>("shadowJar") {
        relocate("de.themoep.minedown", "net.minecrell.serverlistplus.velocity.minedown")

        dependencies {
            include(dependency(libs.minedown.adventure.get()))
        }
    }
}