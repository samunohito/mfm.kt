package com.github.samunohito.mfm.utils

object IntRangeUtils {
  fun mergeIntRange(ranges: Iterable<IntRange>): IntRange {
    return ranges.first().first..ranges.last().last
  }

  fun calcNext(ranges: Iterable<IntRange>): Int {
    return mergeIntRange(ranges).last + 1
  }

  fun calcNext(range: IntRange): Int {
    return range.last + 1
  }

  fun offset(range: IntRange, offset: Int): IntRange {
    return range.first + offset..range.last + offset
  }
}

fun Iterable<IntRange>.merge(): IntRange {
  return IntRangeUtils.mergeIntRange(this)
}

fun IntRange.next(): Int {
  return IntRangeUtils.calcNext(this)
}

fun IntRange.offset(offset: Int): IntRange {
  return IntRangeUtils.offset(this, offset)
}