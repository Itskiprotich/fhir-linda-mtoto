package com.imeja.milk_bank.engine

import android.content.Context
import androidx.work.WorkerParameters
import com.google.android.fhir.sync.AcceptLocalConflictResolver
import com.google.android.fhir.sync.ConflictResolver
import com.google.android.fhir.sync.DownloadWorkManager
import com.google.android.fhir.sync.FhirSyncWorker
import com.imeja.milk_bank.CoreApp


class FhirPeriodicSyncWorker(appContext: Context, workerParams: WorkerParameters) :
    FhirSyncWorker(appContext, workerParams) {

    override fun getConflictResolver() = AcceptLocalConflictResolver

    override fun getDownloadWorkManager(): DownloadWorkManager {
        return DownloadManagerImpl()
    }

    override fun getFhirEngine() = CoreApp.fhirEngine(applicationContext)
}