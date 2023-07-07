package com.github.samunohito.mfm.internal.core.type

data class SeparateResult<T>(
  val all: List<T>
) {
  fun select(index: Int): T {
    if (all.size < index) {
      throw IllegalArgumentException("index out of bounds : $index")
    }

    return all[index]
  }
}