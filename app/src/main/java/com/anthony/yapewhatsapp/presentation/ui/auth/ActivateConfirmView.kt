package com.anthony.yapewhatsapp.presentation.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentActivateConfirmViewBinding


class ActivateConfirmView : Fragment() {
    private lateinit var binding:FragmentActivateConfirmViewBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentActivateConfirmViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(view);
        binding.btnGoToHome.setOnClickListener {
            navController.navigate(R.id.action_activateConfirmView_to_paymentsReceived)
        }
    }

}