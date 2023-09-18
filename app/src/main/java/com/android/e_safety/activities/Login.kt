package com.android.e_safety.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.android.e_safety.R
import com.android.e_safety.models.LoginModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Login : AppCompatActivity() {
    private lateinit var loginBtn: Button
    private lateinit var regbutton: TextView
    private lateinit var splash: TextView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var dbRef: DatabaseReference
    private lateinit var message: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.loginButton)
        regbutton = findViewById(R.id.toReg)
        splash = findViewById(R.id.splashBtn)
        dbRef = FirebaseDatabase.getInstance().getReference("Creds")

        loginBtn.setOnClickListener {
            validate()
            login()
        }
        regbutton.setOnClickListener {
            val reg = Intent(this, Register::class.java)
            startActivity(reg)
        }
        splash.setOnClickListener {
            val splashS = Intent(this, MainActivity::class.java)
            startActivity(splashS)
        }
    }

    private fun validate() : Boolean{
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        val emailPattern = getString(R.string.mailPattern)
        val mailStr = email.text.toString().trim()
        var validateEverything: Boolean
        if(email.text.toString().isEmpty() || password.text.toString().isEmpty()){
            if(email.text.toString().isEmpty())
                email.error = "Please put your email here"
            if(password.text.toString().isEmpty())
                password.error = "Please put your Password here"
            validateEverything = false
            return false
        }else{
            validateEverything = true
            if(mailStr.matches(emailPattern.toRegex()) && validateEverything){
                return true
            }else {
                email.error = "Email not valid"
                return false
            }
        }
    }
    private fun login(){
        email = findViewById(R.id.etEmail)
        password = findViewById(R.id.etPassword)
        message = findViewById(R.id.errorMessage)
        val wrongCreds = "Email or password is incorrect"
        val notFound = "User not found"

        val passStr = password.text.toString()
        if(validate()){

            dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        for(userSnap in snapshot.children){
                            val user = userSnap.getValue(LoginModel::class.java)
                            Log.d("Debug", "Database Password: ${user?.password}, Entered Password: $passStr")
                            if(user?.password?.trim() == passStr.trim()){
                                createDash()
                            }else{
                                message.visibility = View.VISIBLE
                                message.text = wrongCreds
                            }
                        }
                    }else{
                        message.visibility = View.VISIBLE
                        message.text = notFound
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
    private fun createDash(){
        val dash = Intent(this, Dashboard::class.java)
        startActivity(dash)
    }
}