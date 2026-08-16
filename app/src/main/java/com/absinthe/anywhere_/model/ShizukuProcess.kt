package com.absinthe.anywhere_.model

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.absinthe.anywhere_.BuildConfig
import rikka.shizuku.Shizuku
import timber.log.Timber

object ShizukuProcess {

  @Volatile
  private var service: IShizukuService? = null

  @Volatile
  private var binding = false

  private val args by lazy {
    Shizuku.UserServiceArgs(
      ComponentName(BuildConfig.APPLICATION_ID, ShizukuUserService::class.java.name)
    )
      .daemon(false)
      .processNameSuffix("shizuku")
      .debuggable(BuildConfig.DEBUG)
      .version(BuildConfig.VERSION_CODE)
  }

  private val connection = object : ServiceConnection {
    override fun onServiceConnected(name: ComponentName, binder: IBinder) {
      service = IShizukuService.Stub.asInterface(binder)
      binding = false
    }

    override fun onServiceDisconnected(name: ComponentName) {
      service = null
      binding = false
    }
  }

  private val binderReceivedListener = Shizuku.OnBinderReceivedListener { connect() }

  fun start() {
    Shizuku.addBinderReceivedListenerSticky(binderReceivedListener)
  }

  @Synchronized
  fun connect() {
    if (service?.asBinder()?.isBinderAlive == true || binding) return

    binding = true
    try {
      Shizuku.bindUserService(args, connection)
    } catch (e: Throwable) {
      binding = false
      Timber.d(e)
    }
  }

  fun exec(cmd: String): String {
    val current = service?.takeIf { it.asBinder().isBinderAlive } ?: run {
      connect()
      throw IllegalStateException("Shizuku user service is not connected")
    }
    return current.execute(cmd)
  }
}
