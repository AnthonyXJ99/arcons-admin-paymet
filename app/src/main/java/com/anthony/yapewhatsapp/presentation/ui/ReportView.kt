package com.anthony.yapewhatsapp.presentation.ui

import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.anthony.yapewhatsapp.R
import com.anthony.yapewhatsapp.databinding.FragmentReportViewBinding
import com.anthony.yapewhatsapp.presentation.viewmodel.ReportViewModel
import com.anthony.yapewhatsapp.util.GenerateReport
import com.anthony.yapewhatsapp.util.collectFlow
import com.anthony.yapewhatsapp.util.htmlToPdfString
import com.anthony.yapewhatsapp.util.packageInterbank
import com.anthony.yapewhatsapp.util.packagePlin
import com.anthony.yapewhatsapp.util.packagePrueba
import com.anthony.yapewhatsapp.util.packageYape
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date

@AndroidEntryPoint
class ReportView : Fragment() {

    private lateinit var binding: FragmentReportViewBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel by viewModels<ReportViewModel>()

    private val apps = listOf(packageYape, packagePlin, packageInterbank, packagePrueba)
    private var today: String = Date().let { SimpleDateFormat("yyyy-MM-dd").format(it) }

    private var startDateReport: Long? =null
    private var endDateReport: Long? =null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding= FragmentReportViewBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController= Navigation.findNavController(view)
//        binding.reportGeneratedView.loadDataWithBaseURL(null,htmlToPdfString,"text/html","UTF-8",null)
//
        bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE


        /**load initial report**/
        getDateNow()

       lifecycleScope.collectFlow(viewModel.filterByApp(apps,today)){
           if (!it.isNullOrEmpty()) {
               GenerateReport(it,binding.reportGeneratedView,today,today).loadReport()
           }else{
               binding.reportGeneratedView.loadDataWithBaseURL(null,htmlToPdfString,"text/html","UTF-8",null)
           }
       }

        /** buttons**/
        binding.topAppBarReport.setNavigationOnClickListener {
            bottomNavigationView.visibility=View.VISIBLE
            navController.popBackStack()
        }
        binding.btnSharePdf.setOnClickListener {
            createPdf(binding.reportGeneratedView)
        }

        binding.outlinedTextStartDate.setEndIconOnClickListener {
            setDate(binding.inputStartDate)
        }
        binding.outlinedTextEndDate.setEndIconOnClickListener {
            setDate(binding.inputEndDate)
        }


//        binding.inputStartDate.setOnClickListener {
//            setDate(binding.inputStartDate)
//        }
    }

    private fun setDate(editText:TextInputEditText){
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selectedDate ->
            // Aquí puedes manejar la fecha seleccionada
            val date = selectedDate + 5 * 60 * 60 * 1000
            val dateStr= date.let { SimpleDateFormat("yyyy-MM-dd").format(it) }
//            val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(selectedDate)
            editText.setText(dateStr)
            filterByDate()
        }

        picker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun createPdf(webView: WebView) {
        val printManager =requireContext().getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter("Reporte")
        val jobName = getString(R.string.app_name) + " Reporte"
        val attributes = PrintAttributes.Builder()
            .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
            .setResolution(PrintAttributes.Resolution("pdf", "pdf", 600, 600))
            .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
            .build()
        printManager.print(jobName, printAdapter, attributes)
    }

    private fun getDateNow(){
        val currentDate = LocalDate.now()
        binding.inputStartDate.setText("$currentDate")
        binding.inputEndDate.setText("$currentDate")

    }


    private fun filterByDate(){
        val start= binding.inputStartDate.text.toString()
        val end=binding.inputEndDate.text.toString()
        lifecycleScope.collectFlow(viewModel.filterByDate(apps,start,end)){
            if(!it.isNullOrEmpty()){
                GenerateReport(it,binding.reportGeneratedView,start,end).loadReport()
            }else{
                binding.reportGeneratedView.loadDataWithBaseURL(null,htmlToPdfString,"text/html","UTF-8",null)
            }
        }
    }


}