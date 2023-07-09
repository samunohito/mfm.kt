package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmUrl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class UrlAltParserTest {
  private val parser = UrlAltParser()

  @Test
  @DisplayName("match non-ascii characters contained url with angle brackets")
  fun test01() {
    val url = "https://大石泉すき.example.com"
    val input = "<$url>"
    assertUrlAlt(MfmUrl(url, true), input)
  }

  @Test
  @DisplayName("match ascii characters contained url with angle brackets")
  fun test02() {
    val url = "https://example.com#anchor?query=string"
    val input = "<$url>"
    assertUrlAlt(MfmUrl(url, true), input)
  }

  private fun assertUrlAlt(expected: MfmUrl, input: String) {
    val result = parser.parse(input, 0)
    assertEquals(expected.props.url, result.node.props.url)
    assertTrue(result.node.props.brackets ?: false)
  }
}