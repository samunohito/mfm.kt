package com.github.samunohito.mfm.cli

import com.github.samunohito.mfm.api.Mfm

class MfmCliSimple : MfmCliBase() {
  override fun doAction(line: String): Boolean {
    val startAt = System.currentTimeMillis()
    MfmSerializer.serialize(Mfm.parseSimple(line)).let(::println)
    val finishAt = System.currentTimeMillis()

    println("parsing time: ${finishAt - startAt}ms")

    return true
  }

  override fun onStart() {
    println("interactive simple parser. type 'exit' to exit.")
  }
}