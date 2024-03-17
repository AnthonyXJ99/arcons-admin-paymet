package com.anthony.yapewhatsapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentWelcomeViewBinding


class WelcomeView : Fragment() {
   private lateinit var binding: FragmentWelcomeViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        binding= FragmentWelcomeViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view);
        binding.btnGotoLogin.setOnClickListener {
            navController.navigate(R.id.action_welcomeView_to_loginView2)
        }
        binding.btnGoToRegister.setOnClickListener {
            navController.navigate(R.id.action_welcomeView_to_registerView)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.welcomeView) {
                    return
                }
                isEnabled = false


            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

}