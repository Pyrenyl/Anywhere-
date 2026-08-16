package com.absinthe.anywhere_.model

import android.content.Context
import androidx.annotation.Keep
import kotlin.system.exitProcess

@Keep
class ShizukuUserService() : IShizukuService.Stub() {

  constructor(@Suppress("UNUSED_PARAMETER") context: Context) : this()

  override fun execute(command: String): String {
    val process = ProcessBuilder("/system/bin/sh", "-c", command)
      .redirectErrorStream(true)
      .start()
    return try {
      process.inputStream.bufferedReader().use { it.readText() }
    } finally {
      process.destroy()
    }
  }

  override fun destroy() = exitProcess(0)
}
