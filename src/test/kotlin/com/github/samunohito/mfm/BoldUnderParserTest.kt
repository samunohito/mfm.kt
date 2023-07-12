package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmText
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class BoldUnderParserTest {
  private val parser = BoldUnderParser()

  @Test
  fun basic() {
    val input = "__abc__"
    assertEquals(
      MfmText("abc"),
      parser.parse(input, 0).node.children[0]
    )
  }
}