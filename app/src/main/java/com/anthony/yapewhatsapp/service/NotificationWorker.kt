package com.anthony.yapewhatsapp.service

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.util.Log
import androidx.core.app.NotificationCompat
import com.anthony.yapewhatsapp.R


class NotificationWorker: Service(){

    private var context: Context? =null
    private val NOTIFICATION_ID =1
    private val CHANNEL_ID="100"
    private var isDestroyed: Boolean =false

    override fun onBind(p0: Intent?): IBinder? {
        val smg= p0?.getStringExtra("title")
        Log.d("NOTICATION:",smg.toString())
        TODO("Not yet implemented")
    }

    @SuppressLint("ForegroundServiceType")
    override fun onCreate() {
        super.onCreate()
        context=this
        startForeground(NOTIFICATION_ID,showNofication("esperando pagos"))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startService(Intent(this, NotificationListenerService::class.java))
        //doTask()
        return START_STICKY
    }

    /*private fun doTask() {
        TODO("Not yet implemented")
    }*/

    private fun showNofication(content: String): Notification {
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            NotificationChannel(CHANNEL_ID,"Escuchando Notificaciones",NotificationManager.IMPORTANCE_HIGH)
        )
        return NotificationCompat.Builder(
            this,CHANNEL_ID
        ).setContentTitle("Escuchando Notificaciones")
            .setContentText(content)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val jobInfo = JobInfo.Builder(1, ComponentName(this, NotificationWorker::class.java))
            .setPeriodic((1000 * 60 * 60).toLong()) // Cada hora
            .setRequiresCharging(false)
            .build()
        jobScheduler.schedule(jobInfo)
    }



}