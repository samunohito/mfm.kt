package com.github.samunohito.mfm.internal.core.type

data class Result<T>(
  val success: Boolean,
  val value: T?,
  val index: Int,
) {
  fun requireValue(): T {
    return requireNotNull(value)
  }

  fun <U> map(fn: (T) -> U): Result<U> {
    return if (success) {
      Result(true, fn(requireValue()), index)
    } else {
      Result(false, null, -1)
    }
  }

  companion object {
    fun <T> ofSuccess(index: Int, value: T): Result<T> {
      return Result(true, value, index)
    }

    fun <T> ofFailure(): Result<T> {
      return Result(false, null, -1)
    }
  }
}
