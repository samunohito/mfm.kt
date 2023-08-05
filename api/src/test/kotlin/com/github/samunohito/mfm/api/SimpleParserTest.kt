package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.node.MfmEmojiCode
import com.github.samunohito.mfm.api.node.MfmText
import com.github.samunohito.mfm.api.node.MfmUnicodeEmoji
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SimpleParserTest {
  @Nested
  inner class Text {
    @Test
    fun basic() {
      val input = "abc"
      val expect = listOf(MfmText("abc"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `ignore hashtag`() {
      val input = "abc#123"
      val expect = listOf(MfmText("abc#123"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `keycap number sign`() {
      val input = "abc#\uFE0F⃣123"
      val expect = listOf(MfmText("abc"), MfmUnicodeEmoji("#\uFE0F⃣"), MfmText("123"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }
  }

  @Nested
  inner class Emoji {
    @Test
    fun basic() {
      val input = ":foo:"
      val expect = listOf(MfmEmojiCode("foo"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `between texts`() {
      val input = "foo:bar:baz"
      val expect = listOf(MfmText("foo:bar:baz"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `between texts 2`() {
      val input = "12:34:56"
      val expect = listOf(MfmText("12:34:56"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `between texts 3`() {
      val input = "あ:bar:い"
      val expect = listOf(MfmText("あ"), MfmEmojiCode("bar"), MfmText("い"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }

    @Test
    fun `disallow other syntaxes`() {
      val input = "foo **bar** baz"
      val expect = listOf(MfmText("foo **bar** baz"))
      val actual = Mfm.parseSimple(input)
      assertEquals(expect, actual)
    }
  }
}