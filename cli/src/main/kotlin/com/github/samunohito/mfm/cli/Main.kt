package com.github.samunohito.mfm.cli

fun main(args: Array<String>) {
  val cli = when (args.firstOrNull()) {
    "-s", "--simple", "s", "simple" -> MfmCliSimple()
    else -> MfmCli()
  }

  cli.run()
}