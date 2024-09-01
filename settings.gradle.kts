pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "SocialWorkReviewer"
include(":app")

include(":benchmarks")
include(":core:cache")
include(":core:common")
include(":core:design-system")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:datastore-proto")
include(":core:datastore-test")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:screenshot-testing")
include(":core:testing")
include(":core:ui")

include(":feature:about")
include(":feature:announcement")
include(":feature:category")
include(":feature:home")
include(":feature:question")
include(":feature:settings")

include(":framework:countdown-timer")
include(":framework:link-parser")
include(":framework:network-monitor")

include(":lint")
