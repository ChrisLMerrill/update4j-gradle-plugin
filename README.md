# update4j-gradle-plugin
Gradle task to generate a configuration file for update4j (https://github.com/update4j/update4j)



## Usage

I have not yet published this to the public Gradle plugin repository. So first, you must build the plugin from source. Clone the respository and run:

`Run: gradle publishToMavenLocal
`

Next, use the plugin in your project like by adding this to your *build.gradle*

```
plugins {
    id 'update4j-gradle-plugin' version '0.1'
}
```

In order to use a plugin from local Maven repository, you must add this to your *settings.gradle*

```
pluginManagement {
  repositories {
      mavenLocal()
      gradlePluginPortal()
  }
}
```

