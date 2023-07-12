package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmMathBlock
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MathBlockParserTest {
  private val parser = MathBlockParser()

  @Test
  fun `1行の数式ブロックを使用できる`() {
    val input = "\\[math1\\]"
    val output = MfmMathBlock("math1")
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `複数行の数式ブロックを使用できる`() {
    val input = "\\[math1\nmath2\nmath3\\]"
    val output = MfmMathBlock("math1\nmath2\nmath3")
    assertEquals(output, parser.parse(input).node)
  }

  @Test
  fun `行末以外に閉じタグがある場合はマッチしない`() {
    val input = "\\[math1\\]aaa"
    assertFalse(parser.parse(input).success)
  }

  @Test
  fun `行頭以外に開始タグがある場合はマッチしない`() {
    val input = "aaa\\[math1\\]"
    assertFalse(parser.parse(input).success)
  }
}