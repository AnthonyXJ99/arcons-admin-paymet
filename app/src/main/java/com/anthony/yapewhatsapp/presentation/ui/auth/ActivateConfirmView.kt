package com.anthony.yapewhatsapp.presentation.ui.auth

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentActivateConfirmViewBinding
import com.anthony.yapewhatsapp.presentation.viewmodel.ConfirmationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ActivateConfirmView : Fragment() {
    private lateinit var binding:FragmentActivateConfirmViewBinding

    private val confirmationView by viewModels<ConfirmationViewModel>()
    private var isProcess:Boolean =true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentActivateConfirmViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view)
        getDeviceInfo()

        /**Load Confirmation**/
        CoroutineScope(Dispatchers.IO).launch {
           while (isProcess){
               val result=confirmationView.verificationDevice(binding.txtEmei.text.toString().trim())
               if (result.status==200){
                   if (result.data.isActive){
                       withContext(Dispatchers.Main){
                           confirmationView.updateUser()
                           isProcess=false
                           this.cancel()
                       }
                   }

               }
               Log.d("RESPONSE","$result")
               delay(3500)
           }
        }

        if (!isProcess){
            navController.navigate(R.id.action_activateConfirmView_to_paymentsReceived)
        }

        binding.btnGoToHome.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/51935211480?text=Hola%20estoy%20a%20la%20Espera%20la%20activación%20de%20la%20Aplicación,%20mi%20id:")
            startActivity(intent)
//            job.cancel()
//            isProcess=false

            //navController.navigate(R.id.action_activateConfirmView_to_paymentsReceived)
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.activateConfirmView) {
                    return
                }
                isEnabled = false
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
    private fun getDeviceInfo(){
        val id = Settings.Secure.getString(
            requireContext().contentResolver,
            Settings.Secure.ANDROID_ID
        )
        binding.txtEmei.text=id.toString()
    }



}

