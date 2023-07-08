package com.github.samunohito.mfm.internal.core2.parser

interface IParser2<T> {
  fun parse(input: String, startAt: Int = 0): Result2<T>

  fun optional(): IParser2<T?> {
    return Optional(this)
  }

  fun <U> map(fn: (T) -> U): IParser2<U> {
    return Map(fn, this)
  }

  fun nullable(): IParser2<T?> {
    return Nullable(this)
  }

  private class Optional<T>(private val delegate: IParser2<T>) : IParser2<T> {
    override fun parse(input: String, startAt: Int): Result2<T> {
      val result = delegate.parse(input, startAt)
      return if (result.success) {
        result
      } else {
        result.copy(success = true, value = null)
      }
    }
  }

  private class Map<T, U>(private val fn: (T) -> U, private val delegate: IParser2<T>) : IParser2<U> {
    override fun parse(input: String, startAt: Int): Result2<U> {
      val result = delegate.parse(input, startAt)
      return if (result.success) {
        Result2.ofSuccess(result.range, fn.invoke(result.value!!))
      } else {
        Result2.ofFailure()
      }
    }
  }

  private class Nullable<T>(private val delegate: IParser2<T>) : IParser2<T?> {
    override fun parse(input: String, startAt: Int): Result2<T?> {
      val result = delegate.parse(input, startAt)
      return Result2(result.success, result.range, result.value)
    }
  }
}