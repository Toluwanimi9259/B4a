package com.techafresh.b4a

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.techafresh.b4a.adapters.FeedAdapter
import com.techafresh.b4a.databinding.ActivityFeedBinding
import java.io.ByteArrayOutputStream

class FeedActivity : AppCompatActivity() {
    lateinit var binding: ActivityFeedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this@FeedActivity, PostActivity::class.java))
        }
        getTweets()
    }

    private fun getTweets(){
        val query = ParseQuery<ParseObject>("Posts")
        query.findInBackground { objects, e ->
            if (e == null){
                Log.d("MYTAG" , "Number 1 = ${objects[1].getString("Username") + ";;" + objects[1].getParseFile("Image").url}")
                val feedAdapter = FeedAdapter(objects)
                binding.recyclerViewFeed.apply {
                    layoutManager = LinearLayoutManager(this@FeedActivity)
                    adapter = feedAdapter
                }
            }else{
                Log.d("MYTAG" , "Error in Loading Feed = ${e.message}")
                Toast.makeText(this@FeedActivity, "Error in Loading Feed = ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key. The [OnBackPressedDispatcher][.getOnBackPressedDispatcher] will be given a
     * chance to handle the back button before the default behavior of
     * [android.app.Activity.onBackPressed] is invoked.
     *
     * @see .getOnBackPressedDispatcher
     */
    override fun onBackPressed() {
        super.onBackPressed()
        showAlert(ParseUser.getCurrentUser().username, "Sure you wanna quit?")
    }

    private fun showAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes", { dialogInterface, i ->
                ParseUser.logOut()
            })
            .setNegativeButton("No" , { dialogInterface, i ->
                dialogInterface.dismiss()
            })
        val ok = builder.create()
        ok.show()
    }
}