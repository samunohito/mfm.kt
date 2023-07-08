package com.github.samunohito.mfm.internal.core

import com.github.samunohito.mfm.internal.core.type.Params
import com.github.samunohito.mfm.internal.core.type.Result

fun interface IParserHandler<T> {
  fun handle(params: Params): Result<T>
}