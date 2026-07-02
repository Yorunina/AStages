import me.modmuss50.mpp.ReleaseType
import net.minecraftforge.gradle.userdev.tasks.JarJar
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Suppress("PropertyName") val mod_group_id: String by project
@Suppress("PropertyName") val mod_id: String by project
@Suppress("PropertyName") val mod_version: String by project
@Suppress("PropertyName") val mod_name: String by project
@Suppress("PropertyName") val mod_license: String by project
@Suppress("PropertyName") val mod_authors: String by project
@Suppress("PropertyName") val mod_description: String by project

@Suppress("PropertyName") val minecraft_version: String by project
@Suppress("PropertyName") val forge_version: String by project
@Suppress("PropertyName") val mc_version: String by project
@Suppress("PropertyName") val jei_version: String by project
@Suppress("PropertyName") val curios_version: String by project
@Suppress("PropertyName") val minecraft_version_range: String by project
@Suppress("PropertyName") val loader_version_range: String by project
@Suppress("PropertyName") val forge_version_range: String by project
val logoLocation = "https://raw.githubusercontent.com/Alessandro-Casale/AStages/1.20.X/logo/astages.png"


buildscript {
    repositories {
        maven { url = uri("https://repo.spongepowered.org/repository/maven-public/") }
        mavenCentral()
    }

    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

plugins {
    id("eclipse")
    id("idea")
    id("net.minecraftforge.gradle") version "[6.0.16,6.2)"
    id("org.parchmentmc.librarian.forgegradle") version "1.+"
    id("maven-publish")
    id("org.spongepowered.mixin") version "0.7.+"
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

apply(plugin = "org.spongepowered.mixin")

group = mod_group_id
version = mod_version

base {
    archivesName.set(mod_id)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

minecraft {
    mappings("parchment", "2023.09.03-1.20.1")

    copyIdeResources.set(true)
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        configureEach {
            workingDirectory(project.file("run"))

            property("forge.logging.markers", "REGISTRIES")
            property("forge.logging.console.level", "debug")

            mods {
                create(property("mod_id") as String) {
                    source(sourceSets.main.get())
                }
            }
        }

        create("client") {
            property("forge.enabledGameTestNamespaces", property("mod_id") as String)
        }

        create("server") {
            workingDirectory(project.file("run-server"))
            property("forge.enabledGameTestNamespaces", property("mod_id") as String)
            args("--nogui")
        }

        create("gameTestServer") {
            property("forge.enabledGameTestNamespaces", property("mod_id") as String)
        }

        create("data") {
            workingDirectory(project.file("run-data"))

            args(
                "--mod", property("mod_id") as String,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources/")
            )
        }
    }
}

mixin {
    add(sourceSets.main.get(), "${mod_id}.refmap.json")
    config("${mod_id}.mixins.json")
}

sourceSets.main {
    resources {
        srcDir("src/generated/resources")
    }
}

repositories {
    maven {
        name = "Illusive Soulworks maven"
        url = uri("https://maven.theillusivec4.top/")
    }

    maven {
        url = uri("https://maven.architectury.dev")
        content {
            includeGroup("dev.architectury")
        }
    }

    maven {
        url = uri("https://maven.latvian.dev/releases")
        content {
            includeGroup("dev.latvian.mods")
        }
    }

    maven {
        name = "Progwml6's maven"
        url = uri("https://dvs1.progwml6.com/files/maven/")
    }

    maven {
        name = "Jared's maven"
        url = uri("https://maven.blamejared.com/")
    }

    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev")
    }

    maven {
        url = uri("https://www.cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }

    maven {
        url = uri("https://maven.createmod.net/")
    }

    maven {
        name = "Thermal"
        url = uri("https://maven.covers1624.net/")
        content {
            includeGroup("com.teamcofh")
        }
    }

    maven {
        name = "TerraformersMC"
        url = uri("https://maven.terraformersmc.com/")
    }
}

jarJar.enable()

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    // implementation(fg.deobf("curse.maven:botania-225643:5002307"))
    implementation(fg.deobf("curse.maven:curios-309927:5680164"))
    implementation(fg.deobf("curse.maven:patchouli-306770:4966125"))

    implementation(fg.deobf("curse.maven:mekanism-268560:5662583"))

    implementation(fg.deobf("com.simibubi.create:create-1.20.1:6.0.6-152:slim")).let {
        (it as ExternalModuleDependency).isTransitive = false
    }
    implementation(fg.deobf("net.createmod.ponder:Ponder-Forge-${minecraft_version}:1.0.51"))
    compileOnly(fg.deobf("dev.engine-room.flywheel:flywheel-forge-api-${minecraft_version}:1.0.1"))
    runtimeOnly(fg.deobf("dev.engine-room.flywheel:flywheel-forge-${minecraft_version}:1.0.1"))
    implementation(fg.deobf("com.tterrag.registrate:Registrate:MC1.20-1.3.3"))

    implementation(fg.deobf("curse.maven:potionsmaster-356801:4722415"))

    compileOnly(fg.deobf("curse.maven:fastworkbench-288885:5101229"))
    compileOnly(fg.deobf("curse.maven:placebo-283644:5414631"))
    compileOnly(fg.deobf("curse.maven:in-control-257356:5932870"))
    implementation(fg.deobf("curse.maven:aether-255308:6134920"))

    implementation(fg.deobf("dev.latvian.mods:kubejs-forge:2001.6.5-build.14"))
    implementation(fg.deobf("dev.latvian.mods:rhino-forge:2001.2.2-build.18"))
    implementation(fg.deobf("dev.architectury:architectury-forge:9.1.13"))

    compileOnly(fg.deobf("mezz.jei:jei-${mc_version}-forge-api:${jei_version}"))
    implementation(fg.deobf("mezz.jei:jei-${mc_version}-forge:${jei_version}"))
    
    implementation(fg.deobf("curse.maven:cloth-config-348521:5729105"))
    compileOnly(fg.deobf("curse.maven:roughly-enough-items-310111:5846923"))

    implementation(fg.deobf("curse.maven:jade-324717:5339264"))
    implementation(fg.deobf("curse.maven:probejs-585406:5227399"))

    compileOnly(fg.deobf("top.theillusivec4.curios:curios-forge:${curios_version}:api"))
    runtimeOnly(fg.deobf("top.theillusivec4.curios:curios-forge:${curios_version}"))

    compileOnly(fg.deobf("curse.maven:cold-sweat-506194:6258866"))

    implementation("io.github.llamalad7:mixinextras-common:0.4.1")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.4.1")) {
        jarJar.ranged(this, "[0.4.1,)")
    }

    implementation(fg.deobf("curse.maven:ftb-library-forge-404465:6807424"))
    implementation(fg.deobf("curse.maven:ftb-teams-forge-404468:6130786"))
    implementation(fg.deobf("curse.maven:ftb-quests-forge-289412:6829212"))

    implementation(fg.deobf("curse.maven:modernfix-790626:8255312"))
    implementation(fg.deobf("curse.maven:better-modlist-neoforge-1089803:7535242"))
    implementation(fg.deobf("curse.maven:lootr-361276:7263076"))

    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")
}

tasks.named<ProcessResources>("processResources").configure {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "forge_version" to forge_version,
        "forge_version_range" to forge_version_range,
        "loader_version_range" to loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description
    )

    inputs.properties(replaceProperties)

    filesMatching(listOf("META-INF/mods.toml", "pack.mcmeta")) {
        expand(replaceProperties + mapOf("project" to project))
    }
}

tasks.named("jarJar").configure {
    finalizedBy("reobfJarJar")
}

tasks.named<Jar>("jar").configure {
    manifest {
        attributes(
            mapOf(
                "Specification-Title" to mod_id,
                "Specification-Vendor" to mod_authors,
                "Specification-Version" to "1",
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version,
                "Implementation-Vendor" to mod_authors,
                "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                "MixinConfigs" to "${mod_id}.mixins.json"
            )
        )
    }

    dependsOn(tasks.named("jarJar"))
    finalizedBy("reobfJar")
}

publishMods {
    file.set(tasks.named("jarJar", JarJar::class).flatMap { it.archiveFile })
    modLoaders.add("forge")
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate: String = today.format(formatter)
    val changelogFile = layout.projectDirectory.file("CHANGELOG.md")
    val formattedVersion = mod_version.substringBeforeLast("-")

    when {
        mod_version.contains("alpha", true) -> {
            type.set(ALPHA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is an alpha version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        mod_version.contains("beta", true) -> {
            type.set(BETA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is a beta version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        else -> {
            type.set(STABLE)
            changelog.set(providers.fileContents(changelogFile).asText.orElse("No changelog provided."))
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("Alessandro-Casale/AStages")
        val version = mod_version.substringBeforeLast("-")
        val branch = mod_version.substringAfterLast("-")
        commitish.set(branch.toMcRange())
        tagName.set("v$mod_version")

        displayName.set("AStages $mod_version")

        announcementTitle.set("Download from GitHub")
    }

    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))
        projectId.set("1120180")
        minecraftVersions.add(minecraft_version)
        changelogType.set("markdown")
        optional(
            "roughly-enough-items", "jei", "kubejs",
            "in-control", "jade", "fastworkbench"
        )

        displayName.set("astages-$mod_version")

        projectSlug.set("astages") // For discord setup
        announcementTitle.set("Download from CurseForge") // For discord setup
    }

    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_API_KEY"))
        projectId.set("6wy8fmIk")
        minecraftVersions.add(minecraft_version)
        optional(
            "rei", "jei", "kubejs",
            "in-control", "jade"
        )

        displayName.set("astages-$mod_version")

        if (type.get() == ReleaseType.STABLE) {
            changelog.set(
                providers.fileContents(changelogFile)
                    .asText
                    .map { it.lineSequence().drop(3).joinToString("\n") }
            )
        } else {
            changelog.set(changelog.get().dropFirstLine())
        }

        announcementTitle.set("Download from Modrinth")
    }

//    discord {
//        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
//        username.set("AServer")
//        avatarUrl.set(logoLocation)
//        content.set(changelog)
//        setPlatforms(publishMods.platforms["curseforge"], publishMods.platforms["modrinth"])
//
//        style {
//            thumbnailUrl = logoLocation
//            look = "MODERN"
//            link = "BUTTON"
//        }
//    }
}

fun String.toMcRange(): String {
    return this.substringBeforeLast(".") + ".X"
}

fun String.dropFirstLine(): String {
    return lines().drop(1).joinToString("\n")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}
