package com.github.samunohito.mfm.internal.core

import com.github.samunohito.mfm.internal.core.type.Result
import com.github.samunohito.mfm.internal.core.type.SequentialResult
import com.sun.org.slf4j.internal.LoggerFactory

class Parser<T> {
  val name: String?
  val handler: IParserHandler<T>

  constructor(handler: IParserHandler<T>, name: String? = null) {
    this.handler = handler
    this.name = name
  }

  fun <U> map(fn: (T) -> U): Parser<U> {
    return Parser({
      val result = this.handler.handle(it)
      result.map { fn(result.requireValue()) }
    })
  }

  fun text(): Parser<String> {
    return Parser({
      val (input, index) = it
      val result = this.handler.handle(it)
      result.map { input.slice(index..result.index) }
    })
  }

  fun many(min: Int): Parser<List<T>> {
    return Parser({
      var latestIndex = it.index
      val accum = mutableListOf<T>()
      while (latestIndex < it.input.length) {
        val result = this.handler.handle(it)
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

  fun sep(separator: Parser<*>, min: Int): Parser<List<*>> {
    if (min < 1) {
      throw IllegalArgumentException("'min' must be a value greater than or equal to 1.")
    }

    val a1 = this
    val a2 = seq(listOf(separator, this)).select(1).many(min - 1)
    val a3 = seq(listOf(a1, a2)).all()

    return a3.map { listOf(it[0]) + it[1] }
  }

  fun option(): Parser<T?> {
    return altTyped(listOf(nullable(), succeeded(null)))
  }

  fun nullable(): Parser<T?> {
    return Parser({ params ->
      val result = this.handler.handle(params)
      result.map { it }
    })
  }

  fun not(): Parser<T?> {
    return Parser({
      val result = this.handler.handle(it)
      if (result.success) {
        Result.ofFailure()
      } else {
        Result.ofSuccess(it.index, null)
      }
    })
  }

  @Suppress("UNCHECKED_CAST")
  fun <U> typed(): Parser<U> {
    return Parser({ params ->
      val result = this.handler.handle(params)
      result.map { it as U }
    })
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)

    val cr = str("\r")
    val lf = str("\n")
    val crlf = str("\r\n")
    val newLine = alt(listOf(crlf, cr, lf)).typed<String>()
    val space = regexp(Regex("[\u0020\u3000\t]"))
    val alphaAndNum = regexp(Regex("[a-z0-9]", RegexOption.IGNORE_CASE))

    val char = Parser({
      if (it.input.isEmpty()) {
        return@Parser Result.ofFailure()
      }

      val value = it.getCharAtIndex()
      return@Parser Result.ofSuccess(it.index + 1, value.toString())
    })

    val lineBegin = Parser<String?>({
      val index = it.index
      if (index == 0) {
        Result.ofSuccess(index, null)
      } else if (cr.handler.handle(it.copy(index = index - 1)).success) {
        Result.ofSuccess(index, null)
      } else if (lf.handler.handle(it.copy(index = index - 1)).success) {
        Result.ofSuccess(index, null)
      } else {
        Result.ofFailure()
      }
    })

    val lineEnd = Parser<String?>({
      val index = it.index
      if (index == it.input.length) {
        Result.ofSuccess(index, null)
      } else if (cr.handler.handle(it).success) {
        Result.ofSuccess(index, null)
      } else if (lf.handler.handle(it).success) {
        Result.ofSuccess(index, null)
      } else {
        Result.ofFailure()
      }
    })

    fun str(value: String): Parser<String> {
      return Parser({
        val (input, index, _) = it
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
      return Parser({
        val (input, index, _) = it
        val text = input.slice(index..input.length)
        val result = regex.find(text)
        if (result == null) {
          Result.ofFailure()
        } else {
          Result.ofSuccess(index + result.value.length, result.value)
        }
      })
    }

    fun seq(parsers: Collection<Parser<*>>): SequentialResult<*> {
      val result = Parser({ params ->
        val (input, index, _) = params
        var latestIndex = index
        val accum = mutableListOf<Any?>()
        parsers.forEach {
          val result = it.handler.handle(params.copy(index = latestIndex))
          if (!result.success) {
            return@Parser Result.ofFailure()
          }

          latestIndex = result.index
          accum.add(result.requireValue())
        }

        Result.ofSuccess(latestIndex, accum.toList())
      })

      return SequentialResult(result)
    }

    fun seq(vararg parsers: Parser<*>): SequentialResult<*> {
      return seq(parsers.asList())
    }

    private fun <T> succeeded(value: T): Parser<T> {
      return Parser({
        Result.ofSuccess(it.index, value)
      })
    }

    fun alt(parsers: Collection<Parser<*>>): Parser<*> {
      return Parser({
        for (parser in parsers) {
          val result = parser.handler.handle(it)
          if (result.success) {
            return@Parser result
          }
        }

        return@Parser Result.ofFailure()
      })
    }

    fun alt(vararg parsers: Parser<*>): Parser<*> {
      return alt(parsers.asList())
    }

    fun <T> altTyped(parsers: Collection<Parser<T>>): Parser<T?> {
      return Parser({
        for (parser in parsers) {
          val result = parser.handler.handle(it)
          if (result.success) {
            return@Parser Result.ofSuccess(result.index, result.value)
          }
        }

        return@Parser Result.ofFailure()
      })
    }

    fun <T> notMatch(parser: Parser<T>): Parser<T?> {
      return Parser({
        val result = parser.handler.handle(it)
        if (!result.success) {
          Result.ofSuccess(it.index, null)
        } else {
          Result.ofFailure()
        }
      })
    }

    fun <T> lazy(fn: () -> Parser<T>): Parser<T> {
      return Parser({
        val parser = Parser(fn().handler)
        parser.handler.handle(it)
      })
    }
  }
}