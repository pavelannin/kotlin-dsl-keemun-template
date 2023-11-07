pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress(names = ["UnstableApiUsage"])
dependencyResolutionManagement {
    versionCatalogs {
        create("deps") {
            from(files("dependencies.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
    }
}

rootProject.name = "Keemun template"

include(
    "dsl",
    "plantuml-generator",
)