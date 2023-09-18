package com.android.e_safety.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.android.e_safety.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener {

            val loginIntent = Intent(this, Login::class.java)
            startActivity(loginIntent)
        }
        val regbtn = findViewById<Button>(R.id.registerBtn)
        regbtn.setOnClickListener {

            val regIntent = Intent(this, Register::class.java)
            startActivity(regIntent)
        }
        val reportAnon = findViewById<Button>(R.id.reportAnon)
        reportAnon.setOnClickListener {
            val anonIntent = Intent(this, Dashboard::class.java)
            startActivity(anonIntent)
        }
    }
}