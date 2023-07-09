package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmUrl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UrlAltParserTest {
  private val parser = UrlAltParser()

  @Test
  fun `match non-ascii characters contained url with angle brackets`() {
    val url = "https://大石泉すき.example.com"
    val input = "<$url>"
    assertUrlAlt(MfmUrl(url, true), input)
  }

  @Test
  fun `match ascii characters contained url with angle brackets`() {
    val url = "https://example.com#anchor?query=string"
    val input = "<$url>"
    assertUrlAlt(MfmUrl(url, true), input)
  }

  @Test
  fun `ignore label text included url`() {
    val url = "https://大石泉すき.com#anchor?query=string"
    val input = "[<click here>](<$url>)"
    assertUrlAlt(MfmUrl(url, true), input)
  }

  private fun assertUrlAlt(expected: MfmUrl, input: String) {
    val result = parser.parse(input, 0)
    assertEquals(expected.props.url, result.node.props.url)
    assertTrue(result.node.props.brackets ?: false)
  }
}