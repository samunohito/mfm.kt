package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmEmojiCode
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class EmojiCodeParserTest {
  private val parser = EmojiCodeParser()

  @Test
  fun basic() {
    val name = "smile"
    val input = ":$name:"
    val output = MfmEmojiCode(name)

    assertEmojiCode(output, parser.parse(input, 0).node)
  }

  @Test
  fun `before mark missing`() {
    val name = "smile"
    val input = "$name:"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `after mark missing`() {
    val name = "smile"
    val input = ":$name"
    assertFalse(parser.parse(input, 0).success)
  }

  private fun assertEmojiCode(expect: MfmEmojiCode, actual: MfmEmojiCode) {
    assertEquals(expect.props.name, actual.props.name)
  }
}