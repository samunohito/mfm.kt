package com.github.samunohito.mfm.cli

abstract class MfmCliBase : IMfmCli {
  protected open val prompt: String = "> "
  protected open val exitCommand: String = "exit"

  override fun run() {
    onStart()

    while (true) {
      print(prompt)

      val line = readlnOrNull()
        ?.replace(Regex("\\n"), "\n")
        ?.replace(Regex("\\t"), "\t")
        ?.replace(Regex("\\u00a0"), "\u00a0")
        ?: continue

      if (line == exitCommand) {
        break
      }

      if (!doAction(line)) {
        break
      }
    }

    onFinish()
  }

  protected open fun onStart() {
    // nop
  }

  protected open fun onFinish() {
    // nop
  }

  abstract fun doAction(line: String): Boolean
}