package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmUrl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class UrlParserTest {
  private val parser = UrlParser()

  @Test
  fun `basic`() {
    val url = "https://misskey.io/@ai"
    val input = url
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `with other texts`() {
    val url = "https://misskey.io/@ai"
    val input = "official instance: ${url}."
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore trailing period`() {
    val url = "https://misskey.io/@ai"
    val input = "${url}."
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `disallow period only`() {
    val input = "https://."
    assertFalse(parser.parse(input).success)
  }

  @Test
  fun `ignore trailing periods`() {
    val url = "https://misskey.io/@ai"
    val input = "${url}..."
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `with comma`() {
    val url = "https://misskey.io/foo?bar=a,b"
    val input = "$url"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore trailing comma`() {
    val url = "https://misskey.io/foo"
    val input = "$url, bar"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `with brackets`() {
    val url = "https://example.com/foo(bar)"
    val input = "$url"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore parent brackets`() {
    val url = "https://example.com/foo"
    val input = "($url)"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore parent brackets (2)`() {
    val url = "https://example.com/foo"
    val input = "(foo $url)"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore parent brackets with internal brackets`() {
    val url = "https://example.com/foo(bar)"
    val input = "($url)"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore parent squareBrackets`() {
    val url = "https://example.com/foo"
    val input = "foo [$url] bar"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `ignore non-ascii characters contained url without angle brackets`() {
    val url = "https://大石泉すき.example.com"
    val input = "$url"
    val output = MfmUrl(url, false)

    assertUrl(output, parser.parse(input).node)
  }

  @Test
  fun `prevent xss`() {
    val url = "javascript:foo"
    assertFalse(parser.parse(url).success)
  }

  private fun assertUrl(expect: MfmUrl, actual: MfmUrl) {
    assertEquals(expect.props.url, actual.props.url)
    assertEquals(expect.props.brackets, actual.props.brackets)
  }
}