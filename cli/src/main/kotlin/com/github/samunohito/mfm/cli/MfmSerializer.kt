package com.github.samunohito.mfm.cli

import com.google.gson.GsonBuilder

object MfmSerializer {
  fun serialize(node: Any): String {
    return GsonBuilder()
      .serializeNulls()
      .disableHtmlEscaping()
      .disableJdkUnsafe()
//      .setPrettyPrinting()
      .create()
      .toJson(node)
  }
}