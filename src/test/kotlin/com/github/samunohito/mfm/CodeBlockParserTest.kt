package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmBlockCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CodeBlockParserTest {
  private val parser = CodeBlockParser()

  @Test
  fun `コードブロックを使用できる`() {
    val input = "```\nabc\n```"
    val output = MfmBlockCode("abc")
    assertEquals(output, parser.parse(input, 0).node)
  }

  @Test
  fun `コードブロックには複数行のコードを入力できる`() {
    val input = "```\na\nb\nc\n```"
    val output = MfmBlockCode("a\nb\nc")
    assertEquals(output, parser.parse(input, 0).node)
  }

  @Test
  fun `コードブロックは言語を指定できる`() {
    val input = "```js\nconst a = 1;\n```"
    val output = MfmBlockCode("const a = 1;", "js")
    assertEquals(output, parser.parse(input, 0).node)
  }

  @Test
  fun `ignore internal marker`() {
    val input = "```\naaa```bbb\n```"
    val output = MfmBlockCode("aaa```bbb")
    assertEquals(output, parser.parse(input, 0).node)
  }

  @Test
  fun `trim after line break`() {
    val input = "```\nfoo\n```\nbar"
    val output = MfmBlockCode("foo")
    assertEquals(output, parser.parse(input, 0).node)
  }
}