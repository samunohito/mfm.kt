package com.github.samunohito.mfm

import com.github.samunohito.mfm.node.MfmHashtag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class HashtagParserTest {
  private val context = HashtagParser.Context.init()
  private val parser = HashtagParser(context)

  @Test
  fun basic() {
    val tag = "abc"
    val input = "#$tag"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore exclamation mark`() {
    val tag = "Foo"
    val input = "#$tag!"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore colon`() {
    val tag = "Foo"
    val input = "#$tag:"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore single quote`() {
    val tag = "Foo"
    val input = "#$tag'"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore double quote`() {
    val tag = "Foo"
    val input = "#$tag\""
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore square bracket`() {
    val tag = "Foo"
    val input = "#$tag]"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore slash`() {
    val tag = "Foo"
    val input = "#$tag/bar"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `ignore angle bracket`() {
    val tag = "Foo"
    val input = "#$tag<bar>"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `allow including number`() {
    val tag = "foo123"
    val input = "#$tag"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `allow bracket`() {
    val tag = "foo(123)"
    val input = "#$tag"
    assertHashtag(MfmHashtag(tag), parser.parse(input, 0).node)
  }

  @Test
  fun `disallow number only`() {
    val tag = "123"
    val input = "#$tag"
    assertFalse(parser.parse(input, 0).success)
  }

  private fun assertHashtag(expect: MfmHashtag, actual: MfmHashtag) {
    assertEquals(expect.props.hashtag, actual.props.hashtag)
  }
}