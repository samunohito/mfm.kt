# mfm.kt

![](https://github.com/samunohito/mfm.kt/actions/workflows/gradle-ci.yml/badge.svg?branch=master)

[English](README.md) | [日本語](README.ja.md)

mfm.ktは、MFM書式（Misskey Flavored Markdown）を含む文章を構文解析し、プログラム上からMFMを認識しやすいように構文木へと変換するためのKotlinライブラリです。
このプロジェクトは[mfm.js](https://github.com/misskey-dev/mfm.js) の影響を強く受けています。

NOTE: MFM書式そのものについては[mfm.jsのドキュメント](https://github.com/misskey-dev/mfm.js/blob/develop/docs/syntax.md)に詳しい説明があります。

## Installation

mfm.ktはGitHub Packagesに登録されており、Gradleを経由して利用することができます（もちろん、このプロジェクトのリリースから直接jarファイルをダウンロードしても構いません）。
Gradleを経由して利用するためには以下の設定をbuild.gradleに追加してください：

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

NOTE:

- 最新のバージョンについてはReleaseまたはPackagesを参照してください。
- 上記の設定例ではGitHubのユーザー名とトークンを環境変数から取得しています。ご利用の環境にあわせて適切に設定してください（[GitHub Docsの参考ページ](https://docs.github.com/ja/packages/working-with-a-github-packages-registry/working-with-the-gradle-registry#using-a-published-package)）
- このプロジェクトはKotlinで開発されているため、Kotlinのランタイムに依存しています。

## Usage

以下は簡単な使用例です。
各関数の詳細については関数の[KDoc](https://samunohito.github.io/mfm.kt/index.html)を参照してください。

```Kotlin
import com.github.samunohito.mfm.kt.*

val inputText = """
<center>
Hello $[tada everynyan! 🎉]

I'm @ai, A bot of misskey!

https://github.com/syuilo/ai
</center>
"""

// MFMを含む文字列からMFMツリーを生成します。
val mfmTree = Mfm.parse(inputText)

// MFMを含む文字列からシンプルなMFMツリーを生成します。
val simpleMfmTree = Mfm.parseSimple("I like the hot soup :soup:")

// MFMツリーからMFMテキストに逆変換します。
val text = mfmTree.stringify()
```

## Develop

ビルドには JDK 21 が必要です。API と CLI のバイトコードは Java 8 向けです。Kotlin から利用する場合は Kotlin 2.4 のメタデータに対応したコンパイラが必要です。

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

バグ報告や機能追加の提案はGitHubのIssueで受け付けています。また、プルリクエストも歓迎しています。
いまのところ細かいルールは決まっていませんが、後からルールを追加する可能性があります。

## License

MITライセンスが適用されます。