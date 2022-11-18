package com.imeja.milk_bank

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors


open class AppExecutors {
    private val diskIO: Executor = Executors.newSingleThreadExecutor()
    private val networkIO: Executor = Executors.newFixedThreadPool(3)
    private val mMainThread: Executor = MainThreadExecutor()

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainThread(): Executor {
        return mMainThread
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}
