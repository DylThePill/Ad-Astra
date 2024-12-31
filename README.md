# Ad Astra

To add this library to your project, do the following:

Kotlin DSL:
```kotlin
repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
}

dependencies {
    modImplementation(group = "earth.terrarium.adastra", name = "adastra-$modLoader-$minecraftVersion", version = adAstraVersion)
}
```

Groovy DSL:
```groovy
repositories {
    maven { url "https://maven.teamresourceful.com/repository/maven-public/" }
}

dependencies {
    modImplementation group: "earth.terrarium.adastra", name: "adastra-$modLoader-$minecraftVersion", version: adAstraVersion
}
```

### 1.20.1 and Older

Kotlin DSL:
```kotlin
repositories {
    maven(url = "https://maven.teamresourceful.com/repository/maven-public/")
}

dependencies {
    modImplementation(group = "earth.terrarium.adastra", name = "ad_astra-$modLoader-$minecraftVersion", version = adAstraVersion)
}
```

Groovy DSL:
```groovy
repositories {
    maven { url "https://maven.teamresourceful.com/repository/maven-public/" }
}

dependencies {
    modImplementation group: "earth.terrarium.adastra", name: "ad_astra-$modLoader-$minecraftVersion", version: adAstraVersion
}
```