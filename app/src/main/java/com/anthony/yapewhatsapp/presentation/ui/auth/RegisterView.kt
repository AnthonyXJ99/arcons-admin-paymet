package com.anthony.yapewhatsapp.presentation.ui.auth

import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.api.DeviceResponse
import com.anthony.yapewhatsapp.databinding.FragmentRegisterViewBinding
import com.anthony.yapewhatsapp.presentation.viewmodel.RegisterViewModel
import com.anthony.yapewhatsapp.util.TextFieldValidation
import com.anthony.yapewhatsapp.util.ValidationForm
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@AndroidEntryPoint
class RegisterView : Fragment() {

    private lateinit var binding: FragmentRegisterViewBinding
    private val viewModelRegister by viewModels<RegisterViewModel>()

    private var deviceName:String =""
    private var deviceId:String =""
    var navController:NavController? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)
        binding.btnRegisterToWelcome.setOnClickListener {
            navController!!.popBackStack()
        }

        /***validation**/
        binding.registerInputPhone.addTextChangedListener(TextFieldValidation(binding.registerInputPhone,binding.registerInputPhone,binding.outlinedTextNumber))
        binding.registerInputEmail.addTextChangedListener(TextFieldValidation(binding.registerInputEmail,binding.registerInputEmail,binding.outlinedTextEmail))


        /****/

        binding.btnRegister.setOnClickListener {
//            navController!!.navigate(R.id.action_registerView_to_activateConfirmView)
            isLoading(true)
            if (isValidate()){
                if(!binding.registerPolicy.isChecked){
                    showMessage("Acepte nuestros términos y condiciones antes de continuar")
                    isLoading(false)
                    return@setOnClickListener
                }
                lifecycleScope.launch(Dispatchers.IO){
                    val email= binding.registerInputEmail.text.toString()
                    val number= binding.registerInputPhone.text.toString()
                    val device= DeviceResponse(mac =deviceId, email = email, name = deviceName, phone = number, isActive = false)
                    viewModelRegister.registerDevice(device)
                    // delay(1500)
                    withContext(Dispatchers.Main){
                        delay(1500)
                        isLoading(false)
                        val status=viewModelRegister.getStatusResponse().value ?:0
                        val msg=viewModelRegister.getMessageResponse().value ?:"Intentelo nuevamante"
                        if (status==200){
                            delay(1500)
                            navController!!.navigate(R.id.action_registerView_to_activateConfirmView)
                        }else{
                            isLoading(false)
                            showMessage(msg)
                        }
                        Log.d("Mesaje $status",msg)
                    }
                }

            }else{
                isLoading(false)
                showMessage("Valide todos los campos, algunos datos no son correctos")
            }
        }
        getDeviceInfo()

    }

    private fun isValidate():Boolean{
        return ValidationForm().validateEmail(binding.registerInputEmail,binding.outlinedTextEmail) && ValidationForm().validatePhoneNumber(binding.registerInputPhone,binding.outlinedTextNumber)
    }

    private fun getDeviceInfo(){
        val id = Secure.getString(
            requireContext().contentResolver,
            Secure.ANDROID_ID
        )
        val name = Build.BRAND
        deviceName=name
        deviceId=id
        Log.d("DEVICE","{ID:$deviceId, NAME:$deviceName}")
    }


    private fun showMessage(str:String){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Mensaje")
            .setMessage(str)
            .setPositiveButton("Aceptar") { dialog, which ->
                dialog.dismiss()
            }
            .show()
    }
    private fun isLoading(isLoading:Boolean){
        if (isLoading){
            binding.circularLoading.visibility=View.VISIBLE
            binding.btnRegister.visibility=View.GONE
        }else{
            binding.circularLoading.visibility=View.GONE
            binding.btnRegister.visibility=View.VISIBLE
        }
    }

}