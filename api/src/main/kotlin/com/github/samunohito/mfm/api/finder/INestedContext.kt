package com.github.samunohito.mfm.api.finder

interface INestedContext {
  val maximumNestLevel: Int
  var nestLevel: Int
}