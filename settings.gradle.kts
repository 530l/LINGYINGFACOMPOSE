pluginManagement {
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
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-google") }
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public") }
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/jcenter") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven/org/") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven2/") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/android/maven2/") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://jitpack.io") }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-google") }
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/maven-public") }
        maven { url = uri("https://mirrors.cloud.tencent.com/nexus/repository/jcenter") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven/org/") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/maven2/") }
        maven { url = uri("https://mirrors.tuna.tsinghua.edu.cn/android/maven2/") }
        maven { url = uri("https://maven.aliyun.com/repository/google") }
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "LINGYINGFACOMPOSE"
include(":app")
 