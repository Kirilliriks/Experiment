repositories {
    mavenCentral()
}

val imguiVersion = "1.86.4"
dependencies {
    implementation(project(":"))

    implementation("io.github.spair:imgui-java-binding:$imguiVersion")
    implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")
    implementation("io.github.spair:imgui-java-natives-windows:$imguiVersion")
}

tasks.test {
    useJUnitPlatform()
}