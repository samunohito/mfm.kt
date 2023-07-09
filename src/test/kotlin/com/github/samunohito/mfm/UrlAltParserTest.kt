package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmUrl
import org.junit.jupiter.api.Assertions.*
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
  fun `ignore open angle bracket only`() {
    val url = "https://example.com#anchor?query=string"
    val input = "<$url"
    assertFalse(parser.parse(input, 0).success)
  }

  @Test
  fun `ignore close angle bracket only`() {
    val url = "https://example.com#anchor?query=string"
    val input = "$url>"
    assertFalse(parser.parse(input, 0).success)
  }

  private fun assertUrlAlt(expected: MfmUrl, input: String) {
    val result = parser.parse(input, 0)
    assertEquals(expected.props.url, result.node.props.url)
    assertTrue(result.node.props.brackets ?: false)
  }
}