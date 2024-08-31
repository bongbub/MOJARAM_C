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
        jcenter()
        //maven("https://naver.jfrog.io/artifactory/maven/")
        maven("https://repository.map.naver.com/archive/maven")
    }
}

rootProject.name = "mojaram"
include(":app")
 