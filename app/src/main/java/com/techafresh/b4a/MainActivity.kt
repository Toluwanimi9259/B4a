package com.techafresh.b4a

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.parse.ParseException
import com.parse.ParseInstallation
import com.parse.ParseUser
import com.techafresh.b4a.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ParseInstallation.getCurrentInstallation().saveInBackground {
            Toast.makeText(this@MainActivity, "Installation Succesful", Toast.LENGTH_SHORT).show()
        }


        binding.signUpButton.setOnClickListener {
            startActivity(Intent(this@MainActivity , SignUpActivity::class.java))
        }
        binding.loginButton.setOnClickListener {
            login(binding.usernameText.text.toString() , binding.passwordText.text.toString())
        }
    }

    private fun login(username : String , password : String){
        ParseUser.logInInBackground(username, password){ parseUser: ParseUser?, parseException: ParseException? ->
            if (parseUser != null){
                showAlert("Successful Login", "Welcome back " + "<<${username}>>" + " !")
                startActivity(Intent(this@MainActivity, FeedActivity::class.java))
            }else {
                ParseUser.logOut()
                if (parseException != null){
                    Toast.makeText(this, parseException.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
        val ok = builder.create()
        ok.show()
    }
}