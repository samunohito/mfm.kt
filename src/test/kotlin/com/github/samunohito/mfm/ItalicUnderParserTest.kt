package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmText
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ItalicUnderParserTest {
  private val parser = ItalicUnderParser()

  @Test
  fun basic() {
    val input = "_abc_"
    assertEquals(
      MfmText("abc"),
      parser.parse(input, 0).node.children[0]
    )
  }
}