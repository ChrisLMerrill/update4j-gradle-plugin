# update4j-gradle-plugin
Gradle task to generate a configuration file for update4j (https://github.com/update4j/update4j). Familiarity with how to manually build a config file using update4j's Configuration.Builder class will greatly ease understanding and usage of this plugin. I suggest learning how to build a basic config using code first, following the update4j docs.

## Usage

Add an 'update4j' block to build.gradle, as shown below.

* 'uri' is the location of the update files (i.e. the update server) - exactly the same as the uri parameter
* 'path' is the location of the application installation (i.e. on the user's machine)
* 'output' is where the config file will the written (relative to the Gradle build path)
* each 'artifact' specifies a file to be included in the update
* each 'artifacts' specifies a folder to be included in the update (subfolders are not included)

```
update4j {
    uri = 'https://myserver.com/path-to-my-update-files/'
    path = '${user.dir}'
    output = 'update4j/update.xml'
    file = 'name=install/example/lib/example-1.0.jar|path=/lib/example-1.0.jar|classpath'
    folder = 'name=install/example/bin|path=/bin/'
}
```

### file


### folder


## Installation

I have not yet published this to the public Gradle plugin repository. So first, you must build the plugin from source. Clone the respository and run:

`Run: gradle publishToMavenLocal
`

Next, use the plugin in your project like by adding this to your *build.gradle*

```
plugins {
    id 'update4j-gradle-plugin' version '0.2'
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
