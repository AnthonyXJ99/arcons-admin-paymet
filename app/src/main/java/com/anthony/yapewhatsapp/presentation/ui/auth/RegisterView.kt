package com.anthony.yapewhatsapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentRegisterViewBinding


class RegisterView : Fragment() {

    private lateinit var binding: FragmentRegisterViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentRegisterViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view);
        binding.btnRegisterToWelcome.setOnClickListener {
            navController.popBackStack()
        }
        binding.btnRegister.setOnClickListener {
            navController.navigate(R.id.action_registerView_to_activateConfirmView)
        }

    }

}