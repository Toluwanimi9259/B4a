package com.techafresh.b4a

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser
import com.techafresh.b4a.databinding.ActivityPostBinding
import java.io.ByteArrayOutputStream
import java.io.IOException

class PostActivity : AppCompatActivity() {
    lateinit var binding: ActivityPostBinding
    var selectedImage : Bitmap? = null
    var mResultLauncherImage: ActivityResultLauncher<Intent>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Dunno What this is
        mRegisterResultLauncherImage()

        binding.imageViewSelectImageForUpload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this@PostActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@PostActivity,
                    arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                // startActivityForResult is now deprecated apparently
                mResultLauncherImage!!.launch(intent)
            }
        }

        binding.buttonPost.setOnClickListener {
            if (selectedImage != null){
                upload(binding.editTextCaption.text.toString())
            }else{
                Toast.makeText(this@PostActivity, "Select An Image", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun uploadTweet(selectedImage : Uri , caption : String){
        // Converting URI to Bitmap then bytearray
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
        Toast.makeText(this, " Image Received", Toast.LENGTH_SHORT).show()
        Log.d("MainActivity", " Image Received")
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        // Converting bytearray to parse file
        val parseFile = ParseFile("image.jpg", byteArray)
        val parseObject = ParseObject("Posts")
        parseObject.put("Image", parseFile)
        parseObject.put("Username", ParseUser.getCurrentUser().username)
        parseObject.put("Caption", caption)

        // Save File
        parseObject.saveInBackground {
            if (it == null){
                Toast.makeText(this@PostActivity, "Object Saved", Toast.LENGTH_SHORT).show()
            }else{
                showAlert("ERROR" , it.message.toString())
            }
        }
    }

    private fun upload(caption: String = "No Cap"){
        val outputStream = ByteArrayOutputStream()
        selectedImage?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val imageToUpload = outputStream.toByteArray()
        // Converting bytearray to parse file
        val parseFile = ParseFile("image.jpg", imageToUpload)

        val parseObject = ParseObject("Posts")
        parseObject.put("Image", parseFile)
        parseObject.put("Username", ParseUser.getCurrentUser().username)
        parseObject.put("Caption", caption)

        // Save File
        parseObject.saveInBackground {
            if (it == null){
                Toast.makeText(this@PostActivity, "Object Saved", Toast.LENGTH_SHORT).show()
            }else{
                showAlert("ERROR" , it.message.toString())
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

    private fun mRegisterResultLauncherImage() {
        mResultLauncherImage = registerForActivityResult(
            StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == RESULT_OK && data != null) {
                // Image has been selected
                // Converting to bitmap
                try {
                    selectedImage = MediaStore.Images.Media.getBitmap(contentResolver, data.data)
                    // Send Image to ImageView
                    binding.imageViewSelectImageForUpload.setImageBitmap(selectedImage)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            // startActivityForResult is now deprecated apparently
            try {
                mResultLauncherImage?.launch(intent)
            }catch (ex : Exception){
                Log.d("MYTAG ERROR" , "ERROR in REQUEST PERMISSION RESULTS = ${ex.message}")
            }

        }
    }
}