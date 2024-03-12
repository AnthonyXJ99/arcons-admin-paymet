package com.anthony.yapewhatsapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Inicia el servicio después de reiniciar el teléfono
            val serviceIntent = Intent(context, NotificationWorker::class.java)
            context.startService(serviceIntent)
        }
    }
}