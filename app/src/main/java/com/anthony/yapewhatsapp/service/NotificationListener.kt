package com.anthony.yapewhatsapp.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.content.pm.PackageManager
import android.os.Build
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.anthony.yapewhatsapp.data.dao.ContactDao
import com.anthony.yapewhatsapp.data.dao.MessageDao
import com.anthony.yapewhatsapp.domain.model.MessageModel
import com.anthony.yapewhatsapp.util.packageInterbank
import com.anthony.yapewhatsapp.util.packagePlin
import com.anthony.yapewhatsapp.util.packagePrueba
import com.anthony.yapewhatsapp.util.packageYape
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var messageDao: MessageDao

    @Inject
    lateinit var contactDao: ContactDao

    @Inject
    lateinit var ioDispatcher: CoroutineDispatcher


    @SuppressLint("SimpleDateFormat")
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val notiInfo = sbn?.notification ?: return
        if (!checkPermission()){
            Toast.makeText(applicationContext,"Habilite permisos para las notificaciones y el envio Sms",
                Toast.LENGTH_LONG).show()
            return
        }


        CoroutineScope(ioDispatcher).launch {
            val titleMessage = notiInfo.extras.getString(Notification.EXTRA_TITLE)
            val messageContent = notiInfo.extras.getString(Notification.EXTRA_TEXT).toString()
            val subMessage = notiInfo.extras.getString(Notification.EXTRA_SUB_TEXT)

            val packageName = sbn.packageName ?: null
            if( packageName== packageYape || packageName== packagePlin || packageName== packageInterbank || packageName== packagePrueba){
                val postTime = sbn.postTime

                val date = Date(postTime)
                //val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val dateMessage = date.let { SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(it) }

                if(titleMessage!=null) {

                    contactDao.getAll().collect{ contacts->

                        if (!contacts.isNullOrEmpty()){
                            contacts.forEach { item ->
                                val phoneNumber = item.number
                                ContextCompat.getSystemService<SmsManager?>(
                                    applicationContext,
                                    SmsManager::class.java
                                )?.sendTextMessage(phoneNumber, null, messageContent.toString(), null, null)

                            }
                            val message = MessageModel(
                                title = titleMessage.toString(),
                                message = messageContent.toString(),
                                date = dateMessage.toString(),
                                subMessage = subMessage.toString(),
                                appName = packageName.toString()
                            )
                            messageDao.insert(message)
                            Log.d("MYTAG","SE HA GUARDADO CORRECTAMENTE")
                            delay(1500)
                            cancelNotification(sbn.key)
                            //cancelAllNotifications()
                        }
                    }
                }


            }


        }

    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)

        val notiInfo = sbn?.notification ?: return

        val title = notiInfo.extras.getString(Notification.EXTRA_TITLE)
        val text = notiInfo.extras.getString(Notification.EXTRA_TEXT)
        val subText = notiInfo.extras.getString(Notification.EXTRA_SUB_TEXT)

        Log.d("NotificationListener","onNotificationRemoved - title : $title, text : $text, subText : $subText")
    }

    private fun checkPermission():Boolean{
        val permissionStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
       return permissionStatus == PackageManager.PERMISSION_GRANTED && checkSelfPermission( Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
    }

}