package com.techafresh.b4a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.parse.ParseUser
import com.techafresh.b4a.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener {
            Toast.makeText(this@SignUpActivity, "hELOooooooOooOOO", Toast.LENGTH_SHORT).show()
//            signUp("Insulator" , "1234567890")
//            val usernameText = binding.usernameTextS.text
//            val passwordText = binding.passwordTextS.text
//            val confirmPasswordText = binding.confirmPasswordTextS.text
            if (binding.passwordTextS?.text.toString() == binding.confirmPasswordTextS?.text.toString() && !TextUtils.isEmpty(binding.usernameTextS?.text.toString())){
                signUp(binding.usernameTextS?.text.toString(), binding.passwordTextS?.text.toString())
            }else{
                Toast.makeText(this@SignUpActivity, "Please Complete Your Details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signUp(username : String, password : String){
        Log.d("MYTAG" , "IN Sign Up Method")
        val user = ParseUser()
        user.username = username
        user.setPassword(password)
        user.signUpInBackground{e ->
            if (e == null){
                showAlert("Successful Sign Up!" , "Welcome <<${user.username}>>")
                startActivity(Intent(this@SignUpActivity , FeedActivity::class.java))
            }else{
                ParseUser.logOut()
                Log.d("MYTAG SIGNUP ACTIVITY", "ERROR In Sign Up = ${e.message}")
                Toast.makeText(this@SignUpActivity, "ERROR In Sign Up = ${e.message}", Toast.LENGTH_SHORT).show()
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