package com.anthony.yapewhatsapp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> CoroutineScope.collectFlow(flow: Flow<T>, body: suspend (T) -> Unit) {
    flow.onEach { body(it) }
        .launchIn(this)
}

fun String.getDouble():Double{
    val regex = Regex("[0-9.]+")
    val number = regex.find(this)?.value!!.toDouble()
    return number
}

fun getName(str:String): String {
    var name=""
    val firt=firtOption(str)
    if(firt!=null){
        name=firt
    }else{
        name=secondOption(str)
    }
    return name
}

private fun secondOption(str:String):String{
    val palabras = str.split(" ")
    val name = palabras.take(2).joinToString(" ")
    // val name= str.substringAfterLast(" ").substringBefore(" ")
    return name
}

private fun firtOption(str:String):String?{
    val regex = Regex("""(?<=Yape!\s)(.*?)(?=\ste)""")
    val nombre = regex.find(str)?.value
    return nombre
}

fun getPayment(str: String):String{
    return if (str=="com.bcp.innovacxion.yapeapp"){
        "Yape"
    }else{
        "Plin"
    }
}