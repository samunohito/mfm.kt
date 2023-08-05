# mfm.kt

![](https://github.com/samunohito/mfm.kt/actions/workflows/gradle-ci.yml/badge.svg?branch=master)

[English](README.md) | [日本語](README.ja.md)

mfm.kt is a Kotlin library designed to parse text containing MFM format (Misskey Flavored Markdown) and convert it into a syntax tree for easier recognition in programming. This project can be described as a port of [mfm.js](https://github.com/misskey-dev/mfm.js) to Kotlin and is heavily influenced by it.

Note: The [mfm.js documentation](https://github.com/misskey-dev/mfm.js/blob/develop/docs/syntax.md) has a detailed description of the MFM format.

## Installation

mfm.kt is registered on GitHub Packages and can be used via Gradle (of course, you can also download the jar file directly from this project's releases). To use it via Gradle, add the following configuration to your build.gradle:

```gradle
repositories {
    maven {
        url 'https://maven.pkg.github.com/samunohito/mfm.kt'
        credentials {
            username = project.findProperty("gpr.user") ?: System.getenv("USERNAME_GITHUB")
            password = project.findProperty("gpr.token") ?: System.getenv("TOKEN_GITHUB")
        }
    }
}

dependencies {
    implementation 'com.github.samunohito:mfm.kt:最新のバージョン'
}
```

注意:

- For the latest version, please refer to Releases or Packages.
- In the above configuration example, the GitHub username and token are fetched from environment variables. Please configure appropriately for your environment (Reference page on GitHub Docs).
- This project is developed with Kotlin, so it depends on the Kotlin runtime.

## Usage

Below is a simple usage example. For details on each function, please refer to the [KDoc](https://samunohito.github.io/mfm.kt/index.html).

```Kotlin
import com.github.samunohito.mfm.kt.*

val inputText = """
<center>
Hello $[tada everynyan! 🎉]

I'm @ai, A bot of misskey!

https://github.com/syuilo/ai
</center>
"""

// Generate an MFM tree from a string containing MFM.
val mfmTree = Mfm.parse(inputText)

// Generate a simple MFM tree from a string containing MFM.
val simpleMfmTree = Mfm.parseSimple("I like the hot soup :soup:")

// Convert the MFM tree back to MFM text.
val text = mfmTree.stringify()
```

## Develop

```shell
# Clone this repository
git clone https://github.com/samunohito/mfm.kt.git
cd mfm.kt

# Build
./gradlew build

# Run tests
./gradlew test
```

## Contributing

Bug reports and feature requests are accepted via GitHub Issues. Pull requests are also welcome. While there are no specific rules set at the moment, rules may be added in the future.

## License

This project is licensed under the MIT License.