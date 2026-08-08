import me.modmuss50.mpp.ReleaseType
import java.text.SimpleDateFormat
import java.util.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.bundling.AbstractArchiveTask

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
@Suppress("PropertyName") val parchment_minecraft_version: String by project
@Suppress("PropertyName") val parchment_mappings_version: String by project
val logoLocation = "https://raw.githubusercontent.com/Alessandro-Casale/AStages/1.20.X/logo/astages.png"

plugins {
    id("eclipse")
    id("idea")
    id("net.neoforged.moddev.legacyforge") version "2.0.143"
    id("maven-publish")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
}

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

legacyForge {
    version = "${minecraft_version}-${forge_version}"

    validateAccessTransformers = true

    parchment {
        minecraftVersion = parchment_minecraft_version
        mappingsVersion = parchment_mappings_version
    }

    runs {
        create("client") {
            client()
            gameDirectory = project.file("run")
            systemProperty("forge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            gameDirectory = project.file("run-server")
            programArgument("--nogui")
            systemProperty("forge.enabledGameTestNamespaces", mod_id)
        }

        create("gameTestServer") {
            type = "gameTestServer"
            gameDirectory = project.file("run")
            systemProperty("forge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            data()
            gameDirectory = project.file("run-data")
            programArguments.addAll(
                "--mod", mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
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
        name = "Sponge maven"
        url = uri("https://repo.spongepowered.org/repository/maven-public/")
    }

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

dependencies {
    modImplementation("curse.maven:curios-309927:5680164")
    modImplementation("curse.maven:patchouli-306770:4966125")

    modImplementation("curse.maven:mekanism-268560:5662583")

    modImplementation("com.simibubi.create:create-1.20.1:6.0.6-152:slim") {
        isTransitive = false
    }
    modImplementation("net.createmod.ponder:Ponder-Forge-${minecraft_version}:1.0.51")
    modCompileOnly("dev.engine-room.flywheel:flywheel-forge-api-${minecraft_version}:1.0.1")
    modRuntimeOnly("dev.engine-room.flywheel:flywheel-forge-${minecraft_version}:1.0.1")
    modImplementation("com.tterrag.registrate:Registrate:MC1.20-1.3.3")

    modImplementation("curse.maven:potionsmaster-356801:4722415")

    modCompileOnly("curse.maven:fastworkbench-288885:5101229")
    modCompileOnly("curse.maven:placebo-283644:5414631")
    modCompileOnly("curse.maven:in-control-257356:5932870")
    modImplementation("curse.maven:aether-255308:6134920")

    modImplementation("dev.latvian.mods:kubejs-forge:2001.6.5-build.14")
    modImplementation("dev.latvian.mods:rhino-forge:2001.2.2-build.18")
    modImplementation("dev.architectury:architectury-forge:9.1.13")

    modCompileOnly("mezz.jei:jei-${mc_version}-forge-api:${jei_version}")
    modCompileOnly("mezz.jei:jei-${mc_version}-forge:${jei_version}")
    modCompileOnly("curse.maven:emi-580555:8081375")

    modImplementation("curse.maven:cloth-config-348521:5729105")
    modImplementation("curse.maven:roughly-enough-items-310111:5846923")

    modImplementation("curse.maven:jade-324717:5339264")
    modImplementation("curse.maven:probejs-585406:5227399")

    modCompileOnly("top.theillusivec4.curios:curios-forge:${curios_version}:api")
    modRuntimeOnly("top.theillusivec4.curios:curios-forge:${curios_version}")

    modCompileOnly("curse.maven:cold-sweat-506194:6258866")

    implementation("io.github.llamalad7:mixinextras-common:0.4.1")
    annotationProcessor("io.github.llamalad7:mixinextras-common:0.4.1")
    run {
        val mixinExtrasForge = implementation("io.github.llamalad7:mixinextras-forge:0.4.1") as ExternalModuleDependency
        mixinExtrasForge.version {
            strictly("[0.4.1,)")
            prefer("0.4.1")
        }
        add("jarJar", mixinExtrasForge)
    }

    modImplementation("curse.maven:ftb-library-forge-404465:6807424")
    modImplementation("curse.maven:ftb-teams-forge-404468:6130786")
    modImplementation("curse.maven:ftb-quests-forge-289412:6829212")

    modImplementation("curse.maven:modernfix-790626:8255312")
    modImplementation("curse.maven:better-modlist-neoforge-1089803:7535242")
    modCompileOnly("curse.maven:lootr-361276:7263076")
    modCompileOnly("curse.maven:lootjs-570630:7551186")

    modCompileOnly("curse.maven:citadel-331936:7476570")
    modCompileOnly("curse.maven:alexs-caves-924854:4806629")

    modImplementation("curse.maven:radium-reforged-570017:5706069")
    modImplementation("curse.maven:ferritecore-429235:4810975")

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
}

publishMods {
    file.set(tasks.named<AbstractArchiveTask>("reobfJar").flatMap { it.archiveFile })
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
        clientRequired = true
        serverRequired = true

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
