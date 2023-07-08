package com.github.samunohito.mfm.internal.core2.parser

data class Result2<T>(
  val success: Boolean,
  val range: IntRange,
  val value: T?,
) {
  val requireValue = requireNotNull(value)

  companion object {
    fun <T> ofSuccess(range: IntRange, value: T): Result2<T> {
      return Result2(true, range, value)
    }

    fun <T> ofFailure(): Result2<T> {
      return Result2(false, IntRange.EMPTY, null)
    }
  }
}
