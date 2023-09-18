package com.android.e_safety.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.e_safety.R
import com.android.e_safety.models.LoginModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Register : AppCompatActivity() {
    private lateinit var regbutton: Button
    private lateinit var splash: TextView
    private lateinit var loginBtn: TextView
    private lateinit var email: EditText
    private lateinit var password: EditText

    private lateinit var dbRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        loginBtn = findViewById(R.id.toLogin)
        regbutton = findViewById(R.id.regBtn)
        splash = findViewById(R.id.splashBtn)

        dbRef = FirebaseDatabase.getInstance().getReference("Creds")

        regbutton.setOnClickListener {

            if(validate() && validateMail()){
                register()
            }
        }

        loginBtn.setOnClickListener {
            val reg = Intent(this, Login::class.java)
            startActivity(reg)
        }
        splash.setOnClickListener {
            val splashS = Intent(this, MainActivity::class.java)
            startActivity(splashS)
        }
    }

    private fun validate() : Boolean{
        email = findViewById(R.id.etEmail)
        val newPass = findViewById<EditText>(R.id.etNewPass)
        val confirmPass = findViewById<EditText>(R.id.etConfirmPass)
        var emptyStrings = true


//        Check if the fields are empty
        if(email.text.toString().isEmpty()){
            email.error = "Please put your email here"
        }
        if(newPass.text.toString().isEmpty() && confirmPass.text.toString().isEmpty()){
            newPass.error = "Please put your password here"
            confirmPass.error = "Please put your password here"
            emptyStrings = false
        }
        return if(newPass.text.toString() == confirmPass.text.toString() && emptyStrings){
            true
        }else{
            newPass.error = "Passwords do not match"
            confirmPass.error = "Passwords do not match"
            false
        }
    }

    private fun validateMail() :Boolean{
        val emailPattern = getString(R.string.mailPattern)
        val emailStr = email.text.toString().trim()
        return if(emailStr.matches(emailPattern.toRegex())){
            true
        }else{
            email.error = "Email not valid"
            false
        }
    }

    private fun register(){
        val emailStr = email.text.toString()
        password = findViewById(R.id.etNewPass)
        val passStr = password.text.toString()
        if(validate()){
            val empid = dbRef.push().key!!

            val employee = LoginModel(empid, emailStr, passStr)

            dbRef.child(empid).setValue(employee).addOnCompleteListener {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                val newPass = findViewById<EditText>(R.id.etNewPass)
                val confirmPass = findViewById<EditText>(R.id.etConfirmPass)
                email.text.clear()
                newPass.text.clear()
                confirmPass.text.clear()
                val dash = Intent(this, Dashboard::class.java)
                startActivity(dash)
            }.addOnFailureListener { err ->
                Toast.makeText(this, "Error ${err.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}