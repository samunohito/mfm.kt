package com.github.samunohito.mfm.internal.core

object Parsers {
  fun quote() {

  }

  fun codeBlock() {

  }

  fun mathBlock() {

  }

  fun centerTag() {

  }

  fun big() {

  }

  fun boldAsta() {

  }

  fun boldTag() {

  }

  fun boldUnder() {

  }

  fun smallTag() {

  }

  fun italicTag() {

  }

  fun italicAsta() {

  }

  fun italicUnder() {

  }

  fun strikeTag() {

  }

  fun strikeWave() {

  }

  fun unicodeEmoji() {

  }

  fun plainTag() {

  }

  fun fn() {

  }

  fun inlineCode() {

  }

  fun mathInline() {

  }

  fun mention() {

  }

  fun hashtag() {

  }

  fun emojiCode() {
    val side = Parser.regexp(Regex("[a-z0-9]", RegexOption.IGNORE_CASE)).not()

  }

  fun link() {

  }

  fun url() {

  }

  fun urlAlt() {

  }

  fun search() {
    val parseButton = Parser.alt(
      Parser.regexp(Regex("\\[(検索|search)]", RegexOption.IGNORE_CASE)),
      Parser.regexp(Regex("(検索|search)", RegexOption.IGNORE_CASE)),
    )

    val parseQuery = Parser
      .seq(
        Parser
          .alt(
            Parser.newLine,
            Parser.seq(Parser.space, parseButton, Parser.lineEnd).all()
          )
          .not(),
        Parser.char
      )
      .select(1)
      .many(1)

    val parseSearchForm = Parser.seq(
      Parser.newLine.option(),
      Parser.lineBegin,
      parseQuery,
      Parser.space,
      parseButton,
      Parser.lineEnd,
      Parser.newLine.option()
    )

    TODO()
  }

  fun text(): Parser<String> {
    return Parser.char
  }
}