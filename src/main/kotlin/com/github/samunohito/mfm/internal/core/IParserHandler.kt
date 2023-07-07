package com.github.samunohito.mfm.internal.core.type

fun interface IParserHandler<T> {
  fun handle(input: String, index: Int, state: ParserHandlerState): Result<T>
}