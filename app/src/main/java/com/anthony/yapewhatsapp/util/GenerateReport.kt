package com.anthony.yapewhatsapp.util

import android.webkit.WebView
import androidx.compose.runtime.mutableStateListOf
import com.anthony.yapewhatsapp.domain.model.MessageModel
import java.math.BigDecimal
import java.math.RoundingMode

class GenerateReport(private val messages:List<MessageModel>,private val webView: WebView,private val start:String,private val end:String) {

    private var totalAmounts = mutableStateListOf<Double>()

    fun loadReport(){
        webView.loadDataWithBaseURL(null,getHtml(start = start,end),"text/html","UTF-8",null)
    }

    private fun getHtml(start:String,end:String):String{
        var html="""
            <html>
      <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <meta http-equiv="X-UA-Compatible" content="ie=edge" />
        <title>Reporte 2024-03-09 - 2024-03-10</title>

        <style>
          #body_report {
            width: 100%;
          }
          #body_report tr:nth-child(even) {
            background-color: #f2f2f2;
          }
        </style>
      </head>
      <body>
        <div style="padding: 10px">
          <table style="width: 100%">
            <tr style="width: 100%">
              <td style="width: 100%; text-align: center">
                <label
                  style="font-size: 32px; align-content: center; font-weight: bold"
                  >Reporte</label
                >
              </td>
            </tr>
          </table>
          <table style="width: 100%; margin: 10px 0px">
            <tr style="width: 100%">
              <!-- <td style="width: 33%; line-height: 25px">
                <label>From</label><br />
                <label style="font-weight: bold; font-size: 20px"
                  >Business Name</label
                >
                <br />
                Address Line 1 <br />
                Address Line 2 <br />
              </td>
              <td style="width: 33%; line-height: 25px">
                <label>To</label><br />
                <label style="font-weight: bold; font-size: 20px"
                  >Business Name</label
                ><br />
                Address Line 1 <br />
                Address Line 2 <br />
              </td> -->
              <td style="width: 33%; margin: auto">
                <span
                  style="
                    background: #e1e1e1;
                    font-size: 18px;
                    font-weight: bold;
                    padding: 10px;
                    color: #343a40;
                  "
                >
                  $start - $end</span
                >
              </td>
            </tr>
          </table>
          <table id="body_report">
            <tr style="background: #343a40; color: white">
              <th style="padding: 10px">Nombre</th>
              <th>Fecha</th>
              <th>App</th>
              <th>Monto</th>
            </tr>
        """

        val table= generateTable(messages)
        html+=table
        val total= totalAmounts.sum()
        html+="""
            <tfoot>
              <tr style="background: #343a40; color: white">
                <td colspan="3" style="padding: 10px">Total</td>
                <td style="text-align: end; padding-right: 5px">${getAmount(total)}</td>
              </tr>
            </tfoot>
          </table>
        </div>
      </body>
    </html>
        """

        return  html
    }

    private fun generateTable(messages:List<MessageModel>):String{
        var table=""

        messages.forEach {message->
            val amount= getAmount(message.message.getDouble())
            val date=message.date
            val client= getName(message.message)
            val methodPayment= getPayment(message.appName)
            totalAmounts.add(message.message.getDouble())
            table+="""
                <tr>
              <td style="padding: 8px">$client</td>
              <td>$date</td>
              <td>$methodPayment</td>
              <td style="text-align: end; padding-right: 10px">$amount</td>
            </tr>
            """.trimIndent()
        }
        return table;
    }

    private fun getAmount(total:Double):String{
        val bigDecimal = BigDecimal(total).setScale(2, RoundingMode.HALF_UP)
        return  "S/ $bigDecimal"
    }




}