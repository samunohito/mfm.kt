package com.github.samunohito.mfm.api.finder

interface IRecursiveFinderContext {
  val excludeFinders: Set<Class<out ISubstringFinder>>
}