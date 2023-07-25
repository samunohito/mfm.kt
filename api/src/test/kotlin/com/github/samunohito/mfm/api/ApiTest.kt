package com.github.samunohito.mfm.api

import com.github.samunohito.mfm.api.node.MfmEmojiCode
import com.github.samunohito.mfm.api.node.MfmMention
import com.github.samunohito.mfm.api.node.MfmNodeType
import com.github.samunohito.mfm.api.node.MfmText
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class ApiTest {
  @Nested
  inner class ToString {
    @Test
    fun basic() {
      // TODO: everynayn!のあとに🎉が入る予定だが、絵文字は未実装なので…
      val input = """
        before
        <center>
        Hello ${'$'}[tada everynyan!]

        I'm @ai, A bot of misskey!

        https://github.com/syuilo/ai
        </center>
        after
      """.trimIndent()
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun singleNode() {
      val input = "$[tada Hello]"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun quote() {
      val input = """
        > abc
        > 
        > 123
      """.trimIndent()
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun search() {
      val input = "MFM 書き方 123 Search"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun blockCode() {
      val input = "```\nabc\n```"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun mathBlock() {
      val input = "\\[\ny = 2x + 1\n\\]"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun center() {
      val input = "<center>\nabc\n</center>"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun emojiCode() {
      val input = ":abc:"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    @Disabled("絵文字未実装")
    fun unicodeEmoji() {
      val input = "今起きた😇"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun big() {
      val input = "***abc***"
      val output = "$[tada abc]"
      assertEquals(
        output,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun bold() {
      val input = "**abc**"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun small() {
      val input = "<small>abc</small>"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun italicTag() {
      val input = "<i>abc</i>"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun strike() {
      val input = "~~foo~~"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun inlineCode() {
      val input = "AiScript: `#abc = 2`"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun mathInline() {
      val input = "\\(y = 2x + 3\\)"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun hashtag() {
      val input = "a #misskey b"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun link() {
      val input = "[Ai](https://github.com/syuilo/ai)"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun silentLink() {
      val input = "?[Ai](https://github.com/syuilo/ai)"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun fn() {
      val input = "\$[tada Hello]"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun fnWithArguments() {
      val input = "\$[spin.speed=1s,alternate Hello]"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun plain() {
      val input = "a\n<plain>\n**Hello World**\n</plain>\nb"
      assertEquals(
        input,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun plainOneLine() {
      val input = "a\n<plain>**Hello World**</plain>\nb"
      val output = "a\n<plain>\n**Hello World**\n</plain>\nb"
      assertEquals(
        output,
        MfmUtils.stringify(Mfm.parse(input))
      )
    }

    @Test
    fun preserveUrlBrackets() {
      val input1 = "https://github.com/syuilo/ai"
      assertEquals(
        input1,
        MfmUtils.stringify(Mfm.parse(input1))
      )

      val input2 = "<https://github.com/syuilo/ai>"
      assertEquals(
        input2,
        MfmUtils.stringify(Mfm.parse(input2))
      )
    }
  }

  @Nested
  inner class Inspect {
    @Test
    fun replaceText() {
      val input = "good morning $[tada everynyan!]"
      val result = Mfm.parse(input)
      MfmUtils.inspect(result) {
        if (it is MfmText) {
          it.props.text = it.props.text.replace("good morning", "hello")
        }
      }
      assertEquals("hello $[tada everynyan!]", MfmUtils.stringify(result))
    }

    @Test
    fun replaceTextOneItem() {
      val input = "good morning $[tada everyone!]"
      val result = Mfm.parse(input)
      MfmUtils.inspect(result[1]) {
        if (it is MfmText) {
          it.props.text = it.props.text.replace("one", "nyan")
        }
      }
      assertEquals("good morning $[tada everynyan!]", MfmUtils.stringify(result))
    }
  }

  @Nested
  inner class Extract {
    @Test
    fun basic() {
      val nodes = Mfm.parse("@hoge @piyo @bebeyo")
      val expect = listOf(
        MfmMention("hoge", null),
        MfmMention("piyo", null),
        MfmMention("bebeyo", null),
      )
      assertEquals(expect, MfmUtils.extract(nodes) { it.type == MfmNodeType.Mention })
    }

    @Test
    fun nested() {
      val nodes = Mfm.parse("abc:hoge:\$[tada 123 @hoge :foo:]:piyo:")
      val expect = listOf(
        MfmEmojiCode("hoge"),
        MfmEmojiCode("foo"),
        MfmEmojiCode("piyo"),
      )
      assertEquals(expect, MfmUtils.extract(nodes) { it.type == MfmNodeType.EmojiCode })
    }
  }
}