@file:Suppress("DANGEROUS_CHARACTERS", "NonAsciiCharacters")

package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.node.*
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
            MfmMention("ai", "example.com"),
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
          MfmMention("ai", "example.com"),
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
    @DisplayName("コロンの両端が半角英数だった場合は絵文字コードとして解釈されない")
    fun beforeAfterAlphaAndNumeric() {
      val input = "a:abc:1"
      val output = listOf(
        MfmText("a:abc:1")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

  }

  @Nested
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

  @Nested
  inner class InlineCode {
    @Test
    fun basic() {
      val input = "`var x = \"Strawberry Pasta\";`"
      val output = listOf(
        MfmInlineCode("var x = \"Strawberry Pasta\";")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow line break`() {
      val input = "`foo\nbar`"
      val output = listOf(
        MfmText("`foo\nbar`")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow ´`() {
      val input = "`foo´bar`"
      val output = listOf(
        MfmText("`foo´bar`")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class MathInline {
    @Test
    fun basic() {
      val input = "\\(x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}\\)"
      val output = listOf(
        MfmMathInline("x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Mention {
    @Test
    fun basic() {
      val input = "@abc"
      val output = listOf(
        MfmMention("abc", null)
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basic2() {
      val input = "before @abc after"
      val output = listOf(
        MfmText("before "),
        MfmMention("abc", null),
        MfmText(" after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basicRemote() {
      val input = "@abc@example.com"
      val output = listOf(
        MfmMention("abc", "example.com")
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basicRemote2() {
      val input = "before @abc@example.com after"
      val output = listOf(
        MfmText("before "),
        MfmMention("abc", "example.com"),
        MfmText(" after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basicRemote3() {
      val input = "before\n@abc@example.com\nafter"
      val output = listOf(
        MfmText("before\n"),
        MfmMention("abc", "example.com"),
        MfmText("\nafter"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore format of mail address`() {
      val input = "abc@example.com"
      val output = listOf(
        MfmText("abc@example.com"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `detect as a mention if the before char is ^a-z0-9i`() {
      val input = "あいう@abc"
      val output = listOf(
        MfmText("あいう"),
        MfmMention("abc", null),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `invalid char only username`() {
      val input = "@-"
      val output = listOf(
        MfmText("@-"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `invalid char only hostname`() {
      val input = "@abc@."
      val output = listOf(
        MfmText("@abc@."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `allow "-" in username`() {
      val input = "@abc-d"
      val output = listOf(
        MfmMention("abc-d", null),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow "-" in head of username`() {
      val input = "@-abc"
      val output = listOf(
        MfmText("@-abc"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow "-" in tail of username`() {
      val input = "@abc-"
      val output = listOf(
        MfmMention("abc", null),
        MfmText("-"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow period in head of hostname`() {
      val input = "@abc@.aaa"
      val output = listOf(
        MfmText("@abc@.aaa"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow period in tail of hostname`() {
      val input = "@abc@aaa."
      val output = listOf(
        MfmMention("abc", "aaa"),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow "-" in head of hostname`() {
      val input = "@abc@-aaa"
      val output = listOf(
        MfmText("@abc@-aaa"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow "-" in tail of hostname`() {
      val input = "@abc@aaa-"
      val output = listOf(
        MfmMention("abc", "aaa"),
        MfmText("-"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Hashtag {
    @Test
    fun basic() {
      val input = "#abc"
      val output = listOf(
        MfmHashtag("abc"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun basic2() {
      val input = "before #abc after"
      val output = listOf(
        MfmText("before "),
        MfmHashtag("abc"),
        MfmText(" after"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with keycap number sign`() {
      val input = "#️⃣abc123 #abc"
      val output = listOf(
        MfmUnicodeEmoji("#️⃣"),
        MfmText("abc123 "),
        MfmHashtag("abc"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with keycap number sign 2`() {
      val input = "abc\n#️⃣123"
      val output = listOf(
        MfmText("abc\n"),
        MfmUnicodeEmoji("#️⃣"),
        MfmText("123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore a hashtag if the before char is neither a space nor an LF nor ^a-z0-9i`() {
      val f1 = {
        val input = "abc#abc"
        val output = listOf(
          MfmText("abc#abc"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      val f2 = {
        val input = "あいう#abc"
        val output = listOf(
          MfmText("あいう"),
          MfmHashtag("abc"),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      f1.invoke()
      f2.invoke()
    }

    @Test
    fun `ignore comma and period`() {
      val input = "Foo #bar, baz #piyo."
      val output = listOf(
        MfmText("Foo "),
        MfmHashtag("bar"),
        MfmText(", baz "),
        MfmHashtag("piyo"),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore exclamation mark`() {
      val input = "#Foo!"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("!"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore colon`() {
      val input = "#Foo:"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText(":"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore single quote`() {
      val input = "#Foo'"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("'"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore double quote`() {
      val input = "#Foo\""
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("\""),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore square bracket`() {
      val input = "#Foo]"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("]"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore slash`() {
      val input = "#Foo/bar"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("/bar"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore angle bracket`() {
      val input = "#Foo<bar>"
      val output = listOf(
        MfmHashtag("Foo"),
        MfmText("<bar>"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `allow including number`() {
      val input = "#foo123"
      val output = listOf(
        MfmHashtag("foo123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets "()"`() {
      val input = "(#foo)"
      val output = listOf(
        MfmText("("),
        MfmHashtag("foo"),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets "「」"`() {
      val input = "「#foo」"
      val output = listOf(
        MfmText("「"),
        MfmHashtag("foo"),
        MfmText("」"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `mixed brackets`() {
      val input = "「#foo(bar)」"
      val output = listOf(
        MfmText("「"),
        MfmHashtag("foo(bar)"),
        MfmText("」"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets "()" (space before)`() {
      val input = "(bar #foo)"
      val output = listOf(
        MfmText("(bar "),
        MfmHashtag("foo"),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets "「」" (space before)`() {
      val input = "「bar #foo」"
      val output = listOf(
        MfmText("「bar "),
        MfmHashtag("foo"),
        MfmText("」"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow number only`() {
      val input = "#123"
      val output = listOf(
        MfmText("#123"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow number only (with brackets)`() {
      val input = "(#123)"
      val output = listOf(
        MfmText("(#123)"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Url {
    @Test
    fun basic() {
      val input = "https://example.com/@ai"
      val output = listOf(
        MfmUrl("https://example.com/@ai", false),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with other texts`() {
      val input = "official instance: https://example.com/@ai"
      val output = listOf(
        MfmText("official instance: "),
        MfmUrl("https://example.com/@ai", false),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore trailing period`() {
      val input = "https://example.com/@ai."
      val output = listOf(
        MfmUrl("https://example.com/@ai", false),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `disallow period only`() {
      val input = "https://."
      val output = listOf(
        MfmText("https://."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore trailing periods`() {
      val input = "https://example.com/@ai..."
      val output = listOf(
        MfmUrl("https://example.com/@ai", false),
        MfmText("..."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with comma`() {
      val input = "https://example.com/foo?bar=a,b"
      val output = listOf(
        MfmUrl("https://example.com/foo?bar=a,b", false),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore trailing comma`() {
      val input = "https://example.com/foo, bar"
      val output = listOf(
        MfmUrl("https://example.com/foo", false),
        MfmText(", bar"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets`() {
      val input = "https://example.com/foo(bar)"
      val output = listOf(
        MfmUrl("https://example.com/foo(bar)", false),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore parent brackets`() {
      val input = "(https://example.com/foo)"
      val output = listOf(
        MfmText("("),
        MfmUrl("https://example.com/foo", false),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore parent brackets (2)`() {
      val input = "(foo https://example.com/foo)"
      val output = listOf(
        MfmText("(foo "),
        MfmUrl("https://example.com/foo", false),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore parent brackets with internal brackets`() {
      val input = "(https://example.com/foo(bar))"
      val output = listOf(
        MfmText("("),
        MfmUrl("https://example.com/foo(bar)", false),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore parent square brackets`() {
      val input = "foo [https://example.com/foo] bar"
      val output = listOf(
        MfmText("foo ["),
        MfmUrl("https://example.com/foo", false),
        MfmText("] bar"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `ignore non-ascii characters contained url without angle brackets`() {
      val input = "https://本日は晴天なり.example.com"
      val output = listOf(
        MfmText("https://本日は晴天なり.example.com"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `match non-ascii characters contained url with angle brackets`() {
      val input = "<https://本日は晴天なり.example.com>"
      val output = listOf(
        MfmUrl("https://本日は晴天なり.example.com", true),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `prevent xss`() {
      val input = "javascript:foo"
      val output = listOf(
        MfmText("javascript:foo"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Link {
    @Test
    fun basic() {
      val input = "[official instance](https://example.com/@ai)."
      val output = listOf(
        MfmLink(
          false,
          "https://example.com/@ai",
          MfmText("official instance")
        ),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `silent flag`() {
      val input = "?[official instance](https://example.com/@ai)."
      val output = listOf(
        MfmLink(
          true,
          "https://example.com/@ai",
          MfmText("official instance")
        ),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with angle brackets url`() {
      val input = "[official instance](<https://example.com/@ai>)."
      val output = listOf(
        MfmLink(
          false,
          "https://example.com/@ai",
          MfmText("official instance")
        ),
        MfmText("."),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `prevent xss`() {
      val input = "[click here](javascript:foo)"
      val output = listOf(
        MfmText("[click here](javascript:foo)"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Nested
    @DisplayName("cannot nest a url in a link label")
    inner class Nested1 {
      @Test
      fun basic() {
        val input = "official instance: [https://example.com/@ai](https://example.com/@ue)."
        val output = listOf(
          MfmText("official instance: "),
          MfmLink(
            false,
            "https://example.com/@ue",
            MfmText("https://example.com/@ai")
          ),
          MfmText("."),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun nested() {
        val input = "official instance: [https://example.com/@ai**https://example.com/@ue**](https://example.com/@o)."
        val output = listOf(
          MfmText("official instance: "),
          MfmLink(
            false,
            "https://example.com/@o",
            listOf(
              MfmText("https://example.com/@ai"),
              MfmBold(
                MfmText("https://example.com/@ue")
              )
            )
          ),
          MfmText("."),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }
    }

    @Nested
    @DisplayName("cannot nest a link in a link label")
    inner class Nested2 {
      @Test
      fun basic() {
        val input = "official instance: [[https://example.com/@ai](https://example.com/@ue)](https://example.com/@o)."
        val output = listOf(
          MfmText("official instance: "),
          MfmLink(
            false,
            "https://example.com/@ue",
            MfmText("[https://example.com/@ai"),
          ),
          MfmText("]("),
          MfmUrl("https://example.com/@o", false),
          MfmText(")."),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun nested() {
        val input =
          "official instance: [**[https://example.com/@ai](https://example.com/@ue)**](https://example.com/@o)."
        val output = listOf(
          MfmText("official instance: "),
          MfmLink(
            false,
            "https://example.com/@o",
            MfmBold(
              MfmText("[https://example.com/@ai](https://example.com/@ue)")
            ),
          ),
          MfmText("."),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }
    }

    @Nested
    @DisplayName("cannot nest a mention in a link label")
    inner class Nested3 {
      @Test
      fun basic() {
        val input = "[@example](https://example.com)"
        val output = listOf(
          MfmLink(
            false,
            "https://example.com",
            MfmText("@example"),
          ),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun nested() {
        val input = "[@example**@example**](https://example.com)"
        val output = listOf(
          MfmLink(
            false,
            "https://example.com",
            listOf(
              MfmText("@example"),
              MfmBold(
                MfmText("@example"),
              ),
            )
          ),
        )
        assertMfmNodeEquals(output, Mfm.parse(input))
      }
    }

    @Test
    fun `with brackets`() {
      val input = "[foo](https://example.com/foo(bar))"
      val output = listOf(
        MfmLink(
          false,
          "https://example.com/foo(bar)",
          MfmText("foo")
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with parent brackets`() {
      val input = "([foo](https://example.com/foo(bar)))"
      val output = listOf(
        MfmText("("),
        MfmLink(
          false,
          "https://example.com/foo(bar)",
          MfmText("foo")
        ),
        MfmText(")"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with brackets before`() {
      val input = "[test] foo [bar](https://example.com)"
      val output = listOf(
        MfmText("[test] foo "),
        MfmLink(
          false,
          "https://example.com",
          MfmText("bar")
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class Fn {
    @Test
    fun basic() {
      val input = "$[tada abc]"
      val output = listOf(
        MfmFn(
          "tada",
          mapOf(),
          MfmText("abc"),
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with a string argument`() {
      val input = "$[spin.speed=1.1s a]"
      val output = listOf(
        MfmFn(
          "spin",
          mapOf("speed" to "1.1s"),
          MfmText("a"),
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with a string argument 2`() {
      val input = "$[position.x=-3 a]"
      val output = listOf(
        MfmFn(
          "position",
          mapOf("x" to "-3"),
          MfmText("a"),
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `with a string argument 3`() {
      val input = "$[position.x=-3,y=4 abc]"
      val output = listOf(
        MfmFn(
          "position",
          mapOf("x" to "-3", "y" to "4"),
          MfmText("abc"),
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun `invalid fn name`() {
      val input = "$[関数 text]"
      val output = listOf(
        MfmText("$[関数 text]"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun nest() {
      val input = "$[spin.speed=1.1s $[shake a]]"
      val output = listOf(
        MfmFn(
          "spin",
          mapOf("speed" to "1.1s"),
          MfmFn(
            "shake",
            mapOf(),
            MfmText("a"),
          ),
        ),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class PlainTag {
    @Test
    fun multipleLine() {
      val input = "a\n<plain>\n**Hello**\nworld\n</plain>\nb"
      val output = listOf(
        MfmText("a\n"),
        MfmPlain("**Hello**\nworld"),
        MfmText("\nb"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }

    @Test
    fun singleLine() {
      val input = "a\n<plain>**Hello** world</plain>\nb"
      val output = listOf(
        MfmText("a\n"),
        MfmPlain("**Hello** world"),
        MfmText("\nb"),
      )
      assertMfmNodeEquals(output, Mfm.parse(input))
    }
  }

  @Nested
  inner class NestingLimit {
    @Nested
    inner class Quote {
      @Test
      fun basic() {
        val input = ">>> abc"
        val output = listOf(
          MfmQuote(
            MfmQuote(
              MfmText("> abc")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }

      @Test
      fun basic2() {
        val input = ">> **abc**"
        val output = listOf(
          MfmQuote(
            MfmQuote(
              MfmText("**abc**")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Big {
      @Test
      fun basic() {
        val input = "<b><b>***abc***</b></b>"
        val output = listOf(
          MfmBold(
            MfmBold(
              MfmText("***abc***")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Bold {
      @Test
      fun basic() {
        val input = "<i><i>**abc**</i></i>"
        val output = listOf(
          MfmItalic(
            MfmItalic(
              MfmText("**abc**")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }

      @Test
      fun tag() {
        val input = "<i><i><b>abc</b></i></i>"
        val output = listOf(
          MfmItalic(
            MfmItalic(
              MfmText("<b>abc</b>")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Small {
      @Test
      fun basic() {
        val input = "<i><i><small>abc</small></i></i>"
        val output = listOf(
          MfmItalic(
            MfmItalic(
              MfmText("<small>abc</small>")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Italic {
      @Test
      fun basic() {
        val input = "<b><b><i>abc</i></b></b>"
        val output = listOf(
          MfmBold(
            MfmBold(
              MfmText("<i>abc</i>")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Strike {
      @Test
      fun basic() {
        val input = "<b><b>~~abc~~</b></b>"
        val output = listOf(
          MfmBold(
            MfmBold(
              MfmText("~~abc~~")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }

      @Test
      fun tag() {
        val input = "<b><b><s>abc</s></b></b>"
        val output = listOf(
          MfmBold(
            MfmBold(
              MfmText("<s>abc</s>")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }

    @Nested
    inner class Hashtag {
      @Test
      fun `outside ()`() {
        val input = "(#abc)"
        val output = listOf(
          MfmText("("),
          MfmHashtag("abc"),
          MfmText(")"),
        )

        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun `outside square brackets`() {
        val input = "[#abc]"
        val output = listOf(
          MfmText("["),
          MfmHashtag("abc"),
          MfmText("]"),
        )

        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun `outside 「」`() {
        val input = "「#abc」"
        val output = listOf(
          MfmText("「"),
          MfmHashtag("abc"),
          MfmText("」"),
        )

        assertMfmNodeEquals(output, Mfm.parse(input))
      }

      @Test
      fun `outside （）`() {
        val input = "（#abc）"
        val output = listOf(
          MfmText("（"),
          MfmHashtag("abc"),
          MfmText("）"),
        )

        assertMfmNodeEquals(output, Mfm.parse(input))
      }
    }

    @Nested
    inner class Fn {
      @Test
      fun basic() {
        val input = "<b><b>\$[a b]</b></b>"
        val output = listOf(
          MfmBold(
            MfmBold(
              MfmText("\$[a b]")
            )
          ),
        )

        assertMfmNodeEquals(output, Mfm.parse(input, maxNestLevel = 2))
      }
    }
  }
}