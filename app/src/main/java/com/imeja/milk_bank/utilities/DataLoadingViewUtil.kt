package com.imeja.milk_bank.utilities

import android.view.View
import com.imeja.milk_bank.AppExecutors
import com.imeja.milk_bank.databinding.IncludeProgressbarLayoutBinding


fun showLoadingProgress(itemView: IncludeProgressbarLayoutBinding) {
    AppExecutors().mainThread().execute {
        itemView.baseProgressBar.visibility = View.VISIBLE
    }
}

fun hideLoadingProgress(itemView: IncludeProgressbarLayoutBinding) {
    AppExecutors().mainThread().execute {
        itemView.baseProgressBar.visibility = View.GONE
    }

}
