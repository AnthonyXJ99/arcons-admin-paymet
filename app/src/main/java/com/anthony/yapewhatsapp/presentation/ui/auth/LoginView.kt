package com.anthony.yapewhatsapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentLoginViewBinding

class LoginView : Fragment() {

    private lateinit var binding:FragmentLoginViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentLoginViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view);
        binding.btnBackToWelcome.setOnClickListener {
            navController.popBackStack()
        }
        binding.btnStartSession.setOnClickListener {
            navController.navigate(R.id.action_loginView_to_activateConfirmView)
        }
    }


}