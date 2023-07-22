package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.*
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FullParserTest {
  companion object {
    fun assertMfmNodeEquals(expected: List<IMfmNode>, actual: List<IMfmNode>) {
      assertEquals(expected.size, actual.size)
      expected.zip(actual).forEach { (e, a) ->
        assertEquals(e, a)
      }
    }
  }

  @Nested
  inner class Text {
    @Test
    @DisplayName("普通のテキストを入力すると1つのテキストノードが返される")
    fun basic() {
      val input = "abc"
      val output = listOf(
        MfmText("abc")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Quote {
    @Test
    @DisplayName("1行の引用ブロックを使用できる")
    fun singleLine() {
      val input = "> abc"
      val output = listOf(
        MfmQuote(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("複数行の引用ブロックを使用できる")
    fun multipleLine() {
      val input = """
        > abc
        > 123
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmText("abc\n123")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("引用ブロックはブロックをネストできる")
    fun nestableBlock() {
      val input = """
        > <center>
        > a
        > </center>
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmCenter(
            MfmText("a")
          )
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("引用ブロックはインライン構文を含んだブロックをネストできる")
    fun nestableInlineBlock() {
      val input = """
        > <center>
        > I'm @ai@example.com, An bot of misskey!
        > </center>
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmCenter(
            MfmText("I'm "),
            MfmMention("ai", "example.com", "@ai@example.com"),
            MfmText(", An bot of misskey!"),
          )
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("複数行の引用ブロックでは空行を含めることができる")
    fun multipleLinesCanIncludeEmpty() {
      val input = """
        > abc
        >
        > 123
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmText("abc\n\n123")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("1行の引用ブロックを空行にはできない")
    fun singleLinesCanNotEmpty() {
      val input = "> "
      val output = listOf(
        MfmText("> ")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("引用ブロックの後ろの空行は無視される")
    fun ignoreAfterEmptyLine() {
      val input = """
        > foo
        > bar
        
        hoge
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmText("foo\nbar")
        ),
        MfmText("hoge")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("2つの引用行の間に空行がある場合は2つの引用ブロックが生成される")
    fun generateMultipleQuoteBlock() {
      val input = """
        > foo
        
        > bar
        
        hoge
      """.trimIndent()
      val output = listOf(
        MfmQuote(
          MfmText("foo")
        ),
        MfmQuote(
          MfmText("bar")
        ),
        MfmText("hoge")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Search {
    @Nested
    @DisplayName("検索構文を使用できる")
    inner class SearchFormat {
      @Test
      @DisplayName("Search")
      fun test01() {
        val input = "MFM 書き方 123 Search"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      @DisplayName("[Search]")
      fun test02() {
        val input = "MFM 書き方 123 [Search]"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      @DisplayName("search")
      fun test03() {
        val input = "MFM 書き方 123 search"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      @DisplayName("[search]")
      fun test04() {
        val input = "MFM 書き方 123 [search]"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      @DisplayName("検索")
      fun test05() {
        val input = "MFM 書き方 123 検索"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      @DisplayName("[検索]")
      fun test06() {
        val input = "MFM 書き方 123 [検索]"
        val output = listOf(
          MfmSearch("MFM 書き方 123", input)
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }
    }

    @Test
    @DisplayName("ブロックの前後にあるテキストが正しく解釈される")
    fun beforeAndAfter() {
      val input = "abc\nhoge piyo bebeyo 検索\n123"
      val output = listOf(
        MfmText("abc"),
        MfmSearch("hoge piyo bebeyo", "hoge piyo bebeyo 検索"),
        MfmText("123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class CodeBlock {
    @Test
    @DisplayName("コードブロックを使用できる")
    fun basic() {
      val input = "```\nabc\n```"
      val output = listOf(
        MfmBlockCode("abc")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("コードブロックには複数行のコードを入力できる")
    fun basicMultiline() {
      val input = "```\na\nb\nc\n```"
      val output = listOf(
        MfmBlockCode("a\nb\nc")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("コードブロックは言語を指定できる")
    fun lang() {
      val input = "```js\nconst a = 1;\n```"
      val output = listOf(
        MfmBlockCode("const a = 1;", "js")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("ブロックの前後にあるテキストが正しく解釈される")
    fun beforeAfter() {
      val input = "abc\n```\nconst a = 1;\n```\n123"
      val output = listOf(
        MfmText("abc"),
        MfmBlockCode("const a = 1;"),
        MfmText("123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore internal marker`() {
      val input = "```\naaa```bbb\n```"
      val output = listOf(
        MfmBlockCode("aaa```bbb")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `trim after line break`() {
      val input = "```\nfoo\n```\nbar"
      val output = listOf(
        MfmBlockCode("foo"),
        MfmText("bar"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class MathBlock {
    @Test
    @DisplayName("1行の数式ブロックを使用できる")
    fun basic() {
      val input = "\\[math1\\]"
      val output = listOf(
        MfmMathBlock("math1")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("ブロックの前後にあるテキストが正しく解釈される")
    fun beforeAfter() {
      val input = "abc\n\\[math1\\]\n123"
      val output = listOf(
        MfmText("abc"),
        MfmMathBlock("math1"),
        MfmText("123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("行末以外に閉じタグがある場合はマッチしない")
    fun notLineEnd() {
      val input = "\\[aaa\\]after"
      val output = listOf(
        MfmText("\\[aaa\\]after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("行末以外に閉じタグがある場合はマッチしない")
    fun notLineBegin() {
      val input = "before\\[aaa\\]"
      val output = listOf(
        MfmText("before\\[aaa\\]"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Center {
    @Test
    fun `single text`() {
      val input = "<center>abc</center>"
      val output = listOf(
        MfmCenter(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `multiple text`() {
      val input = "before\n<center>\nabc\n123\n\npiyo\n</center>\nafter"
      val output = listOf(
        MfmText("before"),
        MfmCenter(
          MfmText("abc\n123\n\npiyo"),
        ),
        MfmText("after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `nestable inline`() {
      val input = "<center>\nI'm @ai@example.com, An bot of misskey!</center>\n"
      val output = listOf(
        MfmCenter(
          MfmText("I'm "),
          MfmMention("ai", "example.com", "@ai@example.com"),
          MfmText(", An bot of misskey!"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class EmojiCode {
    @Test
    @DisplayName("絵文字コードを使用できる")
    fun basic() {
      val input = ":abc:"
      val output = listOf(
        MfmEmojiCode("abc")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("コロンの直前が半角英数だった場合は絵文字コードとして解釈されない")
    fun beforeAlphaAndNumeric() {
      val input = "a:abc:"
      val output = listOf(
        MfmText("a:abc:")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("コロンの直後が半角英数だった場合は絵文字コードとして解釈されない")
    fun afterAlphaAndNumeric() {
      val input = ":abc:1"
      val output = listOf(
        MfmText(":abc:1")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  @Disabled("現在未実装")
  inner class UnicodeEmoji {
    @Test
    fun basic() {
      val input = "今起きた😇"
      val output = listOf(
        MfmText("今起きた"),
        MfmUnicodeEmoji("😇"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `keycap number sign`() {
      val input = "abc#️⃣123"
      val output = listOf(
        MfmText("abc"),
        MfmUnicodeEmoji("#️⃣"),
        MfmText("123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Big {
    @Test
    fun basic() {
      val input = "***abc***"
      val output = listOf(
        MfmFn("tada", mapOf(), MfmText("abc"))
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("内容にはインライン構文を利用できる")
    fun usableInline() {
      val input = "***123**abc**123***"
      val output = listOf(
        MfmFn(
          "tada", mapOf(), listOf(
            MfmText("123"),
            MfmBold(
              MfmText("abc")
            ),
            MfmText("123"),
          )
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    @DisplayName("内容は改行できる")
    fun lineBreak() {
      val input = "***123\n**abc**\n123***"
      val output = listOf(
        MfmFn(
          "tada", mapOf(), listOf(
            MfmText("123\n"),
            MfmBold(
              MfmText("abc")
            ),
            MfmText("\n123"),
          )
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class BoldTag {
    @Test
    fun basic() {
      val input = "<b>abc</b>"
      val output = listOf(
        MfmBold(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `inline syntax allowed inside`() {
      val input = "<b>123~~abc~~456</b>"
      val output = listOf(
        MfmBold(
          MfmText("123"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `line breaks`() {
      val input = "<b>123\n~~abc~~\n456</b>"
      val output = listOf(
        MfmBold(
          MfmText("123\n"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("\n456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Bold {
    @Test
    fun basic() {
      val input = "**abc**"
      val output = listOf(
        MfmBold(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `inline syntax allowed inside`() {
      val input = "**123~~abc~~456**"
      val output = listOf(
        MfmBold(
          MfmText("123"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `line breaks`() {
      val input = "**123\n~~abc~~\n456**"
      val output = listOf(
        MfmBold(
          MfmText("123\n"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("\n456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class SmallTag {
    @Test
    fun basic() {
      val input = "<small>abc</small>"
      val output = listOf(
        MfmSmall(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `inline syntax allowed inside`() {
      val input = "<small>123~~abc~~456</small>"
      val output = listOf(
        MfmSmall(
          MfmText("123"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `line breaks`() {
      val input = "<small>123\n~~abc~~\n456</small>"
      val output = listOf(
        MfmSmall(
          MfmText("123\n"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("\n456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class ItalicTag {
    @Test
    fun basic() {
      val input = "<i>abc</i>"
      val output = listOf(
        MfmItalic(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `inline syntax allowed inside`() {
      val input = "<i>123~~abc~~456</i>"
      val output = listOf(
        MfmItalic(
          MfmText("123"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `line breaks`() {
      val input = "<i>123\n~~abc~~\n456</i>"
      val output = listOf(
        MfmItalic(
          MfmText("123\n"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("\n456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class ItalicAlt1 {
    @Test
    fun basic() {
      val input = "*abc*"
      val output = listOf(
        MfmItalic(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basic2() {
      val input = "before *abc* after"
      val output = listOf(
        MfmText("before "),
        MfmItalic(
          MfmText("abc")
        ),
        MfmText(" after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore a italic syntax if the before char is neither a space nor an LF nor ^a-z0-9i`() {
      val f1 = {
        val input = "before*abc*after"
        val output = listOf(
          MfmText("before*abc*after"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      val f2 = {
        val input = "あいう*abc*えお"
        val output = listOf(
          MfmText("あいう"),
          MfmItalic(
            MfmText("abc")
          ),
          MfmText("えお"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      f1.invoke()
      f2.invoke()
    }
  }

  @Nested
  inner class ItalicAlt2 {
    @Test
    fun basic() {
      val input = "_abc_"
      val output = listOf(
        MfmItalic(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basic2() {
      val input = "before _abc_ after"
      val output = listOf(
        MfmText("before "),
        MfmItalic(
          MfmText("abc")
        ),
        MfmText(" after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore a italic syntax if the before char is neither a space nor an LF nor ^a-z0-9i`() {
      val f1 = {
        val input = "before_abc_after"
        val output = listOf(
          MfmText("before_abc_after"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      val f2 = {
        val input = "あいう_abc_えお"
        val output = listOf(
          MfmText("あいう"),
          MfmItalic(
            MfmText("abc")
          ),
          MfmText("えお"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      f1.invoke()
      f2.invoke()
    }
  }

  @Nested
  inner class StrikeTag {
    @Test
    fun basic() {
      val input = "<s>abc</s>"
      val output = listOf(
        MfmStrike(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `inline syntax allowed inside`() {
      val input = "<s>123~~abc~~456</s>"
      val output = listOf(
        MfmStrike(
          MfmText("123"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `line breaks`() {
      val input = "<s>123\n~~abc~~\n456</s>"
      val output = listOf(
        MfmStrike(
          MfmText("123\n"),
          MfmStrike(
            MfmText("abc")
          ),
          MfmText("\n456"),
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class StrikeWave {
    @Test
    fun basic() {
      val input = "~~abc~~"
      val output = listOf(
        MfmStrike(
          MfmText("abc")
        )
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }
}