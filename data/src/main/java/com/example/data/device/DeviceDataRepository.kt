package com.example.data.device

import android.app.AppOpsManager
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.net.ConnectivityManager
import kotlinx.coroutines.withContext
import java.util.Calendar
import android.os.Process
import kotlinx.coroutines.Dispatchers

class DeviceDataRepository(private val context: Context) {

    /**
     * Checks if the user has gone into Settings and allowed app to see usage data
     */

    fun hasUsagePermissions(): Boolean{
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(),
            context.packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    /**
     * Queries the hardware for bytes transmitted/received over mobile data
     * since the start of the current month
     */

    //suspend allows the app to pause this function without freezing the entire screen
    suspend fun getMobileDataUsageBytes(): Long = withContext(Dispatchers.IO){ //forces code to run on background thread optimized for I/O
        if(!hasUsagePermissions()) return@withContext 0L

        val networkStatsManager = context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager

        //calculate start of month (emulating typical billing period

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)

        val startTime = calendar.timeInMillis
        val endTime = System.currentTimeMillis()

        try {
            //querySummaryForDevice aggregates usage across all apps
            //subscriberId = null means "All SIMs"
            val bucket = networkStatsManager.querySummaryForDevice(
                ConnectivityManager.TYPE_MOBILE,
                null,
                startTime,
                endTime
            )

            //return total = received + transmitted
            return@withContext bucket.rxBytes + bucket.txBytes
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext 0L
        }
    }
}