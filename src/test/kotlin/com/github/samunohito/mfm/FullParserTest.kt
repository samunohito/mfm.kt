package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.*
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
}