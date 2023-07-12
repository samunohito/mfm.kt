package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmText
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ItalicAstaParserTest {
  private val parser = ItalicAstaParser()

  @Test
  fun basic() {
    val input = "*abc*"
    assertEquals(
      MfmText("abc"),
      parser.parse(input, 0).node.children[0]
    )
  }
}