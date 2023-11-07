plugins {
    kotlin("jvm") version "1.8.22"
    `maven-publish`
    signing
}

val PUBLISH_GROUP_ID = "io.github.pavelannin"
val PUBLISH_ARTIFACT_ID = "keemun-dsl"
val PUBLISH_VERSION = "1.0.0"

group = PUBLISH_GROUP_ID
version = PUBLISH_VERSION

kotlin {
    jvmToolchain(8)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                groupId = PUBLISH_GROUP_ID
                artifactId = PUBLISH_ARTIFACT_ID
                version = PUBLISH_VERSION
                from(project.components.getByName("java"))

                java.withSourcesJar()
                java.withJavadocJar()

                pom {
                    name.set(PUBLISH_ARTIFACT_ID)
                    description.set("Keemun kotlin dsl")
                    url.set("https://github.com/pavelannin/kotlin-dsl-keemun-template")
                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("http://www.opensource.org/licenses/mit-license.php")
                        }
                    }
                    developers {
                        developer {
                            name.set("Pavel Annin")
                            email.set("pavelannin.dev@gmail.com")
                        }
                    }
                    scm {
                        connection.set("scm:git:github.com/pavelannin/kotlin-dsl-keemun-template.git")
                        developerConnection.set("scm:git:ssh://github.com/pavelannin/kotlin-dsl-keemun-template.git")
                        url.set("https://github.com/pavelannin/kotlin-dsl-keemun-template/tree/main")
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
