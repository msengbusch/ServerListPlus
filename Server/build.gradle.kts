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

dependencies {
    implementation(libs.netty)
    implementation(libs.guava)
    implementation(libs.snakeyaml)
    implementation(libs.gson)

    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
}

tasks {
    getByName<Jar>("jar") {
        manifest.attributes(mapOf("Main-Class" to "net.minecrell.serverlistplus.server.Main"))
    }

    getByName<ShadowJar>("shadowJar") {
        dependencies {
            include(dependency(libs.netty.get()))

            include(dependency(libs.guava.get()))
            include(dependency(libs.snakeyaml.get()))
            include(dependency(libs.gson.get()))
        }
    }
}
