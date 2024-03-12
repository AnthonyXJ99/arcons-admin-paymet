package com.anthony.yapewhatsapp.presentation.ui

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.ActivityMainBinding
import com.anthony.yapewhatsapp.presentation.viewmodel.HomeViewModel
import com.anthony.yapewhatsapp.service.NotificationListener
import com.anthony.yapewhatsapp.service.NotificationWorker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Home : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private val REQUEST_CODE_NOTIFICATIONS= 123
    private var foreGroundServiceIntent: Intent? =null


    // viewModel
    private val homeViewModel by viewModels<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**navigation**/
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)


        //binding.bottomNavigation.isVisible=false

        /**Notification permission**/



        val permissionStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            applicationContext.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
//        val storagePermission= checkStoragePermissions()

        if (permissionStatus == PackageManager.PERMISSION_GRANTED && checkSelfPermission( Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            // El permiso ya está concedido
            // Mostrar notificaciones
        } else {
            // El permiso no está concedido
            // Solicitar el permiso al usuario
            val permissions = arrayOf(Manifest.permission.POST_NOTIFICATIONS,Manifest.permission.SEND_SMS,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permissions, REQUEST_CODE_NOTIFICATIONS)
        }

        if (!notificationAccessPermCheck()){
            getPermission()
        }else{
            toggleNotificationListenerService()
        }

        foreGroundServiceIntent= Intent(this, NotificationWorker::class.java)
        startService(foreGroundServiceIntent)



    }

    private fun getPermission(){

        val dialog = AlertDialog.Builder(this)
            .setTitle("Acceso a notificaciones")
            .setMessage("Esta aplicación necesita acceder a las notificaciones para poder del dispositivo")
            .setPositiveButton("Aceptar") { dialog, _ ->
                Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS").also {
                    startActivity(it)
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        dialog.show()
    }

    private fun toggleNotificationListenerService() {
        val pm = packageManager
        pm.setComponentEnabledSetting(
            ComponentName(applicationContext, NotificationListener::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
        pm.setComponentEnabledSetting(
            ComponentName(applicationContext, NotificationListener::class.java),
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP
        )
    }

    private fun notificationAccessPermCheck(): Boolean {
        val sets = NotificationManagerCompat.getEnabledListenerPackages(this)
        return sets.contains(packageName)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_NOTIFICATIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // El usuario ha denegado el permiso
                // Mostrar un mensaje al usuario
            }
        }
    }

}