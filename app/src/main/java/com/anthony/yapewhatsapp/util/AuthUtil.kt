package com.anthony.yapewhatsapp.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.anthony.yapewhatsapp.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TextFieldValidation(private val view: View, private val input:TextInputEditText,private val layout:TextInputLayout ) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {}
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // checking ids of each text field and applying functions accordingly.
        when (view.id) {
            R.id.registerInputPhone -> {
                ValidationForm().validatePhoneNumber(input,layout)
            }
            R.id.registerInputEmail -> {
                ValidationForm().validateEmail(input,layout)
            }
            R.id.txtNumberContact->{
                ValidationForm().validatePhoneNumber(input,layout)
            }
        }
    }
}

class ValidationForm{
    fun validateEmail(input:TextInputEditText,layout:TextInputLayout): Boolean {
        if (input.text.toString().trim().isEmpty()) {
            layout.error = "Este campo es obligatorio!"
            input.requestFocus()
            return false
        } else if (!isValidEmail(input.text.toString())) {
            layout.error = "Correo no válido!"
            input.requestFocus()
            return false
        } else {
            layout.isErrorEnabled = false
        }
        return true
    }
    fun validatePhoneNumber(input:TextInputEditText,layout:TextInputLayout):Boolean{

        if (input.text.toString().trim().isEmpty()){
            layout.error = "Correo no válido!"
            input.requestFocus()
        }else if (!isValidatePhone(input.text.toString())){
            layout.error = "Número no válido: Ej 51996332221"
            input.requestFocus()
        }else{
            layout.isErrorEnabled=false
        }
        return true
    }

    private fun isValidatePhone(str: String): Boolean {
        val regex = Regex("^519[0-9]{8}\$")
        return regex.matches(str)
    }

    private fun isValidEmail(str: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }
}