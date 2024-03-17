package com.anthony.yapewhatsapp.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.runtime.mutableStateListOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentPaymentsReceivedBinding
import com.anthony.yapewhatsapp.domain.model.MessageModel
import com.anthony.yapewhatsapp.presentation.ui.adapter.MessageCallback
import com.anthony.yapewhatsapp.presentation.ui.adapter.MessagesAdapter
import com.anthony.yapewhatsapp.presentation.viewmodel.HomeViewModel
import com.anthony.yapewhatsapp.util.collectFlow
import com.anthony.yapewhatsapp.util.getDouble
import com.anthony.yapewhatsapp.util.lstPackages
import com.anthony.yapewhatsapp.util.packageInterbank
import com.anthony.yapewhatsapp.util.packagePlin
import com.anthony.yapewhatsapp.util.packageYape
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class PaymentsReceived : Fragment(),MessageCallback{

    private lateinit var binding: FragmentPaymentsReceivedBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var paymentAmounts = mutableStateListOf<Double>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsReceivedBinding.inflate(inflater,container,false)
        return binding.root
    }
    private var today: String = Date().let { SimpleDateFormat("yyyy-MM-dd").format(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        val navController = Navigation.findNavController(view)

        /**RECYCLER ADAPTER**/

        val manager = LinearLayoutManager(this.context,  LinearLayoutManager.VERTICAL, false)
        val adapter = MessagesAdapter(this)
        binding.rclMessages.layoutManager=manager
        binding.rclMessages.adapter=adapter

        /**Launch messages from database**/
        lifecycleScope.collectFlow(homeViewModel.filterByApp(lstPackages,today)){messages->
            if(messages!=null){
                adapter.submitList(messages)
                addAmount(messages)
            }
        }

        binding.filterAll.setOnClickListener {
            lifecycleScope.collectFlow(homeViewModel.filterByApp(lstPackages,today)){messages->
                if(messages!=null){
                    adapter.submitList(messages)
                    addAmount(messages)
                }
            }
        }

        binding.filterYape.setOnClickListener {
            lifecycleScope.collectFlow(homeViewModel.filterByApp(listOf(packageYape),today)){ messages->
                if(messages!=null){
                    adapter.submitList(messages)
                    addAmount(messages)
                }
            }
        }

        binding.filterPlin.setOnClickListener {
            lifecycleScope.collectFlow(homeViewModel.filterByApp(listOf(packagePlin,
                packageInterbank),today)){ messages->
                if(messages!=null){
                    adapter.submitList(messages)
                    addAmount(messages)
                }
            }
        }

        binding.btnReport.setOnClickListener {
            navController.navigate(R.id.action_paymentsReceived_to_reportView)
        }



        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.currentDestination?.id == R.id.paymentsReceived) {
                    return
                }
                isEnabled = false


            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }


    private fun addAmount(messages:List<MessageModel>){
        paymentAmounts.clear()
        messages.forEach {message->
            val amount= message.message.getDouble()
           lifecycleScope.launch {
               paymentAmounts.add(amount)
           }
        }
        val total= paymentAmounts.sum()
        val bigDecimal = BigDecimal(total).setScale(2, RoundingMode.HALF_UP)
        binding.txtTotal.text="S/ $bigDecimal"
    }

    override fun onDeleteItem(message: MessageModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            homeViewModel.deleteMessage(message)
        }
    }




}