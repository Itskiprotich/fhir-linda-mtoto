package com.imeja.milk_bank

import android.app.Application
import android.content.Context
import com.google.android.fhir.*
import com.google.android.fhir.sync.Sync
import com.imeja.milk_bank.constants.Constants.DEMO_SERVER
import com.imeja.milk_bank.engine.FhirPeriodicSyncWorker

class CoreApp : Application() {
    private val fhirEngine: FhirEngine by lazy { constructFhirEngine() }
    private lateinit var instance: Context
    override fun onCreate() {
        super.onCreate()
        instance = this.applicationContext
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

    }
}