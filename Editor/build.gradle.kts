import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

repositories {
    mavenCentral()
}

plugins {
    id("java")
    id("java-library")
    id("com.github.johnrengelman.shadow")
}


val imguiVersion = "1.86.4"
dependencies {
    implementation(project(":"))

    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-windows:$imguiVersion")
}

tasks {
    build {
        dependsOn(shadowJar) // Run shadowJar on build
    }
}

tasks.withType<ShadowJar> {
    manifest {
        attributes["Manifest-Version"] = "1.0"
        attributes["Main-Class"] = "org.anotherteam.EditorLauncher"
    }

    archiveFileName.set("Editor.jar")
}

tasks.test {
    useJUnitPlatform()
}