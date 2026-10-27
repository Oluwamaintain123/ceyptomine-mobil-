package com.cryptomine.mobile

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder

/**
 * Fail-closed service. It deliberately does not claim to mine until a reviewed,
 * Android-compatible native engine is included and its process integration tested.
 */
class MiningService : Service() {
    companion object {
        const val ACTION_START = "com.cryptomine.mobile.START"
        const val ACTION_STOP = "com.cryptomine.mobile.STOP"
        const val EXTRA_POOL = "pool"
        const val EXTRA_WALLET = "wallet"
        const val EXTRA_THREADS = "threads"
        private const val CHANNEL = "cryptomine_status"
        private const val NOTIFICATION_ID = 42
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_STOP -> stopSelf()
            ACTION_START -> {
                createChannel()
                startForeground(NOTIFICATION_ID, notification("Mining engine unavailable"))
                // Do not launch arbitrary files or silently download executable code.
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL, "CryptoMine status", NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
        }
    }

    private fun notification(message: String): Notification {
        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, CHANNEL)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        return builder.setContentTitle("CryptoMine Mobile")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(false)
            .build()
    }
}
