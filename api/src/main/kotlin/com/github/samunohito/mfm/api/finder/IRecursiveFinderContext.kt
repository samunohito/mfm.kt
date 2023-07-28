package com.github.samunohito.mfm.api.finder

interface IRecursiveFinderContext : INestedContext {
  val excludeFinders: Set<Class<out ISubstringFinder>>
  fun renew(
    maximumNestLevel: Int = this.maximumNestLevel,
    nestLevel: Int = this.nestLevel,
    excludeFinders: Set<Class<out ISubstringFinder>> = this.excludeFinders,
  ): IRecursiveFinderContext {
    return Impl(
      maximumNestLevel = maximumNestLevel,
      nestLevel = nestLevel,
      excludeFinders = excludeFinders,
    )
  }

  data class Impl(
    override val maximumNestLevel: Int = 10,
    override var nestLevel: Int = 0,
    override val excludeFinders: Set<Class<out ISubstringFinder>> = emptySet(),
  ) : IRecursiveFinderContext
}