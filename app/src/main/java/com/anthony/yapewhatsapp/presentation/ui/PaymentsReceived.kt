package com.anthony.yapewhatsapp.presentation.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anthony.yapewhatsapp.databinding.FragmentPaymentsReceivedBinding
import com.anthony.yapewhatsapp.domain.model.MessageModel
import com.anthony.yapewhatsapp.presentation.ui.adapter.MessageCallback
import com.anthony.yapewhatsapp.presentation.ui.adapter.MessagesAdapter
import com.anthony.yapewhatsapp.presentation.viewmodel.HomeViewModel
import com.anthony.yapewhatsapp.util.collectFlow
import com.anthony.yapewhatsapp.util.packageInterbank
import com.anthony.yapewhatsapp.util.packagePlin
import com.anthony.yapewhatsapp.util.packagePrueba
import com.anthony.yapewhatsapp.util.packageYape
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.Date


@AndroidEntryPoint
class PaymentsReceived : Fragment(),MessageCallback{

    private lateinit var binding: FragmentPaymentsReceivedBinding
    private val homeViewModel by viewModels<HomeViewModel>()
    private var paymentAmounts = mutableStateListOf<Double>()

    private var startDateReport: Long? =null
    private var endDateReport: Long? =null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPaymentsReceivedBinding.inflate(inflater,container,false)
        return binding.root
    }
    private val apps = listOf(packageYape, packagePlin, packageInterbank, packagePrueba)
    private var today: String = Date().let { SimpleDateFormat("yyyy-MM-dd").format(it) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**RECYCLER ADAPTER**/

        val manager = LinearLayoutManager(this.context,  LinearLayoutManager.VERTICAL, false)
        val adapter: MessagesAdapter = MessagesAdapter(this)
        binding.rclMessages.layoutManager=manager
        binding.rclMessages.adapter=adapter

        /**Launch messages from database**/
        lifecycleScope.collectFlow(homeViewModel.filterByApp(apps,today)){messages->
            if(messages!=null){
                adapter.submitList(messages)
                addAmount(messages)
            }
        }

        binding.filterAll.setOnClickListener {
            lifecycleScope.collectFlow(homeViewModel.filterByApp(apps,today)){messages->
                if(messages!=null){
                    adapter.submitList(messages)
                    addAmount(messages)
                }
            }
        }

        binding.filterYape.setOnClickListener {

            lifecycleScope.collectFlow(homeViewModel.filterByApp(listOf(packagePrueba),today)){ messages->
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

            firstDate()

        }

        /**delete messages**/
//        binding.btnCleanData.setOnClickListener {
//            lifecycleScope.launch(Dispatchers.IO) {
//                homeViewModel.deleteAllMessage()
//            }
//        }
    }

    private fun getAmount(msg:String):Double{
        val regex = Regex("[0-9.]+")
        val numero = regex.find(msg)?.value!!.toDouble()
        return numero
    }
    private fun addAmount(messages:List<MessageModel>){
        paymentAmounts.clear()
        messages.forEach {message->
            val amount= getAmount(message.message)
           lifecycleScope.launch {
               paymentAmounts.add(amount)
           }
        }
        val total= paymentAmounts.sum()
        val bigDecimal = BigDecimal(total).setScale(2, RoundingMode.HALF_UP)
        binding.txtTotal.text="S/ $bigDecimal"
    }

    private fun firstDate(){
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha Inicio")
                .build()
        datePicker.show(parentFragmentManager,null)
        datePicker.addOnCancelListener {
            startDateReport=null
        }
        datePicker.addOnPositiveButtonClickListener {date ->
            startDateReport=date
            secondDate()
        }
    }
    private fun secondDate(){
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Fecha Fin")
                .build()
        datePicker.show(parentFragmentManager,null)
        datePicker.addOnCancelListener {
            startDateReport=null
            endDateReport=null
        }
        datePicker.addOnPositiveButtonClickListener {date->
            endDateReport=date
            compareDate()

            lifecycleScope.collectFlow(homeViewModel.filterByApp(apps,today)){
                if(it!=null){
                    val intent=sharePdf(it)
                    startActivity(intent)
                }
            }
        }
    }

    private fun compareDate(){
       if (startDateReport!=null  && endDateReport!=null){
           if(startDateReport==endDateReport){
               Log.d("FECHA ES:" ,"IGUAL")
           }else if(startDateReport!! < endDateReport!!){
               Log.d("FECHA ES:" ,"Valida")
           }else{
               Log.d("FECHA ES:" ,"Invalida")
           }
       }
    }

    fun generatePdf(data: List<MessageModel>): File {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val paint = Paint()
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = 16.0f
        canvas.drawText("Reporte de datos", pageInfo.pageWidth / 2.0f, 30.0f, paint)

        paint.textSize = 12.0f
        paint.textAlign = Paint.Align.LEFT
        var y: Float = 50.0f

        // Dibujar la tabla
        data.forEach {message->
            canvas.drawText(message.message, 10.0f, y, paint)
            canvas.drawText(message.title, 110.0f, y, paint)
            canvas.drawText(getAmount(message.message).toString(), 210.0f, y, paint)
            y += 20.0f
        }

        document.finishPage(page)
        val tempFile = File.createTempFile("my_report", ".pdf")
        val outputStream = FileOutputStream(tempFile)
        document.writeTo(outputStream)
        outputStream.close()

        document.close()
        return tempFile
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun sharePdf(data: List<MessageModel>): Intent {

        val file = generatePdf(data)

        val uri = context?.let {
            FileProvider.getUriForFile(
                it,
                it.opPackageName + ".provider",
                file
            )
        }




        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "application/pdf"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        return intent
    }


    override fun onDeleteItem(message: MessageModel) {
        lifecycleScope.launch(Dispatchers.IO) {
            homeViewModel.deleteMessage(message)
        }
    }



}