package com.imeja.milk_bank

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.android.fhir.*
import com.google.android.fhir.sync.Sync
import com.imeja.milk_bank.constants.Constants.DEMO_SERVER
import com.imeja.milk_bank.constants.Constants.TAG_LOGIN
import com.imeja.milk_bank.engine.FhirPeriodicSyncWorker
import timber.log.Timber

class CoreApp : Application() {
    private val fhirEngine: FhirEngine by lazy { constructFhirEngine() }
    private lateinit var instance: Context
    private val sharedPrefFile = "milk-bank"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instance = this.applicationContext
        sharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        FhirEngineProvider.init(
            FhirEngineConfiguration(
                enableEncryptionIfSupported = true,
                DatabaseErrorStrategy.RECREATE_AT_OPEN,
                ServerConfiguration(DEMO_SERVER)
            )
        )
        Sync.oneTimeSync<FhirPeriodicSyncWorker>(this)
    }

    private fun constructFhirEngine(): FhirEngine {
        return FhirEngineProvider.getInstance(this)
    }

    companion object {
        fun fhirEngine(context: Context) =
            (context.applicationContext as CoreApp).fhirEngine

        fun setSignedIn(context: Context, b: Boolean) {
            (context.applicationContext as CoreApp).editor.putBoolean(TAG_LOGIN, b).commit()
        }

        fun isSignedIn(context: Context): Boolean {
            return (context.applicationContext as CoreApp).sharedPreferences.getBoolean(
                TAG_LOGIN,
                false
            )
        }

    }
}