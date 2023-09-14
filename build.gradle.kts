import java.util.*

plugins {
    id("java")
    id("java-library")
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        setUrl("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

tasks.compileJava {
    options.release.set(16)
    options.encoding = "UTF-8"
    options.isFork = true
}

ext {
    set("mainClassName", "org.anotherteam.Experimental")
    set("assetsDir", File("src/assets/"))
    set("imguiVersion", "1.86.4")
}

sourceSets.main.get().java.srcDir("src/")
sourceSets.main.get().resources.srcDirs("src/assets/")

tasks.register<JavaExec>("runApp") {
    classpath = sourceSets.main.get().runtimeClasspath

    setStandardInput(System.`in`)
    workingDir = File("src/")
    setIgnoreExitValue(true)
    //args = "appArg1"
}

val lwjglVersion = "3.3.1"
val lwjglNatives = "natives-windows"
val imguiVersion = "1.86.4"

allprojects {
    group = "org.anotherteam"
    version = "0.1"

    apply(plugin = "java")
    apply(plugin = "java-library")

    dependencies {
        compileOnly("org.jetbrains:annotations:23.0.0")

        implementation("org.joml:joml:1.10.4")

        implementation("com.google.code.gson:gson:2.10")

        implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

        implementation("org.lwjgl:lwjgl")
        implementation("org.lwjgl:lwjgl-assimp")
        implementation("org.lwjgl:lwjgl-bgfx")
        implementation("org.lwjgl:lwjgl-cuda")
        implementation("org.lwjgl:lwjgl-egl")
        implementation("org.lwjgl:lwjgl-glfw")
        implementation("org.lwjgl:lwjgl-jawt")
        implementation("org.lwjgl:lwjgl-jemalloc")
        implementation("org.lwjgl:lwjgl-libdivide")
        implementation("org.lwjgl:lwjgl-llvm")
        implementation("org.lwjgl:lwjgl-lmdb")
        implementation("org.lwjgl:lwjgl-lz4")
        implementation("org.lwjgl:lwjgl-meow")
        implementation("org.lwjgl:lwjgl-nanovg")
        implementation("org.lwjgl:lwjgl-nfd")
        implementation("org.lwjgl:lwjgl-nuklear")
        implementation("org.lwjgl:lwjgl-odbc")
        implementation("org.lwjgl:lwjgl-openal")
        implementation("org.lwjgl:lwjgl-opencl")
        implementation("org.lwjgl:lwjgl-opengl")
        implementation("org.lwjgl:lwjgl-opengles")
        implementation("org.lwjgl:lwjgl-openvr")
        implementation("org.lwjgl:lwjgl-opus")
        implementation("org.lwjgl:lwjgl-ovr")
        implementation("org.lwjgl:lwjgl-par")
        implementation("org.lwjgl:lwjgl-remotery")
        implementation("org.lwjgl:lwjgl-rpmalloc")
        implementation("org.lwjgl:lwjgl-shaderc")
        implementation("org.lwjgl:lwjgl-sse")
        implementation("org.lwjgl:lwjgl-stb")
        implementation("org.lwjgl:lwjgl-tinyexr")
        implementation("org.lwjgl:lwjgl-tinyfd")
        implementation("org.lwjgl:lwjgl-tootle")
        implementation("org.lwjgl:lwjgl-vma")
        implementation("org.lwjgl:lwjgl-vulkan")
        implementation("org.lwjgl:lwjgl-xxhash")
        implementation("org.lwjgl:lwjgl-yoga")
        implementation("org.lwjgl:lwjgl-zstd")
        runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-bgfx::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-jemalloc::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-libdivide::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-llvm::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-lmdb::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-lz4::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-meow::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-nanovg::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-nfd::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-nuklear::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-opengles::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-openvr::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-opus::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-ovr::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-par::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-remotery::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-rpmalloc::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-shaderc::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-sse::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-tinyexr::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-tinyfd::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-tootle::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-vma::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-xxhash::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-yoga::$lwjglNatives")
        runtimeOnly("org.lwjgl:lwjgl-zstd::$lwjglNatives")

        implementation("io.github.spair:imgui-java-binding:$imguiVersion")
        implementation("io.github.spair:imgui-java-lwjgl3:$imguiVersion")

        implementation("io.github.spair:imgui-java-natives-windows:$imguiVersion")
    }
}

tasks.register<GradleBuild>("runDebug") {
    tasks = listOf("clean", "build", "copyTo")
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = project.ext.get("mainClassName")
    }

    from(configurations.compileClasspath.get().map { if (it.isDirectory()) it else zipTree(it) })

    tasks.register<Copy>("copyTo") {
        from("src/assets")
        into("build/libs/assets")
    }

    tasks.register<Copy>("copyFrom") {
        from("build/libs/assets")
        into("src/assets")
    }
}