pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "MovieApp"
include(":app")
include(":core")
include(":core:ui")
include(":core:designsystem")
include(":core:data")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:common")
include(":feature")
include(":feature:login")
include(":feature:signup")
include(":feature:movie_details")
include(":feature:see_all")
include(":feature:movies")
include(":feature:profile")
include(":feature:search")
include(":feature:watch_list")
include(":core:navigation")
