package com.github.samunohito.mfm.internal.core.type

import com.sun.org.slf4j.internal.LoggerFactory

class Parser<T> {
  val name: String?
  val handler: IParserHandler<T>

  constructor(handler: IParserHandler<T>, name: String? = null) {
    this.handler = IParserHandler { input, index, state ->
      if (state.trace && name != null) {
        val paddedPos = index.toString().padEnd(6, ' ')
        log.debug("${paddedPos}enter $name")

        val result = handler.handle(input, index, state)
        if (result.success) {
          val paddedPosWithResult = "$index:${result.index}".padEnd(6, ' ')
          log.debug("${paddedPosWithResult}match $name")
        } else {
          log.debug("${paddedPos}fail $name")
        }

        result
      } else {
        handler.handle(input, index, state)
      }
    }

    this.name = name
  }

  fun <U> map(fn: (T) -> U): Parser<U> {
    return Parser({ input, index, state ->
      val result = this.handler.handle(input, index, state)
      result.map { fn(result.requireValue()) }
    })
  }

  fun text(): Parser<String> {
    return Parser({ input, index, state ->
      val result = this.handler.handle(input, index, state)
      result.map { input.slice(index..result.index) }
    })
  }

  fun many(min: Int): Parser<List<T>> {
    return Parser({ input, index, state ->
      var latestIndex = index
      val accum = mutableListOf<T>()
      while (latestIndex < input.length) {
        val result = this.handler.handle(input, index, state)
        if (!result.success) {
          break
        }

        latestIndex = result.index
        accum.add(result.requireValue())
      }

      if (accum.size < min) {
        Result.ofFailure()
      } else {
        Result.ofSuccess(latestIndex, accum)
      }
    })
  }

//  fun sep(separator: Parser<*>, min: Int): Parser<List<Any>> {
//    if (min < 1) {
//      throw IllegalArgumentException("'min' must be a value greater than or equal to 1.")
//    }
//
//    return seq(listOf(this, seq(listOf(separator, this), 1).many(min - 1))).map { listOf(it[0]) + (it[1] as List<Any>) })
//  }

//  fun <T> option(): Parser<T?> {
//    return alt(listOf(this, succeeded(null)))
//  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
    val cr = str("\r")
    val lf = str("\n")
    val crlf = str("\r\n")

    val char = Parser({ input, index, _ ->
      if ((input.length - index) < 1) {
        return@Parser Result.ofFailure()
      }

      val value = input[index]
      return@Parser Result.ofSuccess(index + 1, value)
    })

    val lineBegin = Parser({ input, index, state ->
      if (index == 0) {
        Result.ofSuccess(index, null)
      } else if (cr.handler.handle(input, index - 1, state).success) {
        Result.ofSuccess(index, null)
      } else if (lf.handler.handle(input, index - 1, state).success) {
        Result.ofSuccess(index, null)
      } else {
        Result.ofFailure()
      }
    })

    val lineEnd = Parser({ input, index, state ->
      if (index == input.length) {
        Result.ofSuccess(index, null)
      } else if (cr.handler.handle(input, index, state).success) {
        Result.ofSuccess(index, null)
      } else if (lf.handler.handle(input, index, state).success) {
        Result.ofSuccess(index, null)
      } else {
        Result.ofFailure()
      }
    })

    fun str(value: String): Parser<String> {
      return Parser({ input, index, _ ->
        if ((input.length - index) < value.length) {
          Result.ofFailure()
        } else if (input.substring(index, value.length) != value) {
          Result.ofFailure()
        } else {
          Result.ofSuccess(index + value.length, value)
        }
      })
    }

    fun regexp(regex: Regex): Parser<String> {
      return Parser({ input, index, _ ->
        val text = input.slice(index..input.length)
        val result = regex.find(text)
        if (result == null) {
          Result.ofFailure()
        } else {
          Result.ofSuccess(index + result.value.length, result.value)
        }
      })
    }

    fun seq(parsers: Collection<Parser<Any?>>, select: Int? = null): Parser<Any?> {
      return Parser({ input, index, state ->
        var latestIndex = index
        val accum = mutableListOf<Any?>()
        parsers.forEach {
          val result = it.handler.handle(input, latestIndex, state)
          if (!result.success) {
            return@Parser result
          }

          latestIndex = result.index
          accum.add(result.requireValue())
        }

        val result = if (select != null) {
          listOf(accum[select])
        } else {
          accum
        }

        Result.ofSuccess(latestIndex, result)
      })
    }

    private fun <T> succeeded(value: T): Parser<T> {
      return Parser({ _, index, _ ->
        Result.ofSuccess(index, value)
      })
    }

    fun alt(parsers: Collection<Parser<*>>): Parser<*> {
      return Parser({ input, index, state ->
        for (parser in parsers) {
          val result = parser.handler.handle(input, index, state)
          if (result.success) {
            return@Parser result
          }
        }

        return@Parser Result.ofFailure()
      })
    }

    fun notMatch(parser: Parser<*>): Parser<*> {
      return Parser({ input, index, state ->
        val result = parser.handler.handle(input, index, state)
        if (!result.success) {
          Result.ofSuccess(index, null)
        } else {
          Result.ofFailure()
        }
      })
    }

    fun <T> lazy(fn: () -> Parser<T>): Parser<T> {
      return Parser({ input, index, state ->
        val parser = Parser(fn().handler)
        parser.handler.handle(input, index, state)
      })
    }
  }
}