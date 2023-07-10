package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmInlineCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class InlineCodeParserTest {
  private val parser = InlineCodeParser()

  @Test
  fun basic() {
    val input = "`var x = \"Strawberry Pasta\";`"
    val output = MfmInlineCode("var x = \"Strawberry Pasta\";")
    assertEquals(output.props.code, parser.parse(input, 0).node.props.code)
  }

  @Test
  fun `disallow line break`() {
    val input = "`foo\nbar`"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `disallow ´`() {
    val input = "`foo´bar`"
    assertFalse(parser.parse(input, 0).success)
  }
}