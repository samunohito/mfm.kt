package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmMathInline
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MathInlineParserTest {
  private val parser = MathInlineParser()

  @Test
  fun basic() {
    val input = "\\(x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}\\)"
    val output = MfmMathInline("x = {-b \\pm \\sqrt{b^2-4ac} \\over 2a}")
    assertEquals(output.props.formula, parser.parse(input, 0).node.props.formula)
  }
}