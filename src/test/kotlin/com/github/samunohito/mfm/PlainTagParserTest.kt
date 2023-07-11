package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmText
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PlainTagParserTest {
  private val parser = PlainTagParser()

  @Test
  fun `multiple line`() {
    val input = """
      <plain>
      This is a test.
      Hello, world!
      abc 123
      </plain>
    """.trimIndent()

    val result = parser.parse(input, 0).node
    assertEquals(
      MfmText(
        """
        This is a test.
        Hello, world!
        abc 123
        """.trimIndent()
      ),
      result.children.first()
    )
  }

  @Test
  fun `single line`() {
    val input = "<plain>本日は晴天なり</plain>"

    val result = parser.parse(input, 0).node
    assertEquals(
      MfmText("本日は晴天なり"),
      result.children.first()
    )
  }
}