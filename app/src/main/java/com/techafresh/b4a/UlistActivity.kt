package com.techafresh.b4a

import android.R
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.parse.ParseUser
import com.techafresh.b4a.adapters.ListAdapter
import com.techafresh.b4a.databinding.ActivityUlistBinding

class UlistActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUlistBinding
    var userList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUlistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getAllUsers()
    }
    private fun getAllUsers(){
        val query = ParseUser.getQuery()
        Log.d("MYTAG" , "Current User = ${ParseUser.getCurrentUser().username}")
        query.addAscendingOrder("username")
        query.whereNotEqualTo("username" , ParseUser.getCurrentUser().username)
        query.findInBackground { objects, e ->
            if (e == null) {
                Toast.makeText(this@UlistActivity, " No Exception", Toast.LENGTH_SHORT).show()
                if (objects.size > 0) {
                    Toast.makeText(this@UlistActivity, " Users are not empty ", Toast.LENGTH_SHORT).show()
                    for (`object` in objects) {
//                        val x = `object`.username
                        userList.add(`object`.username)
                    }
                    initRecyclerView(userList)
                    Toast.makeText(applicationContext, " UserList  " + userList.toString(), Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, " No data was gotten  ", Toast.LENGTH_LONG).show()
                }
            } else {
                Toast.makeText(
                    this@UlistActivity,
                    " There was a problem" + e.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        query.findInBackground { objects, e ->
            if (e == null){
//                Log.d("MYTAG" , "Current User = ${ParseUser.getCurrentUser().username}")
//                Toast.makeText(this, "Current User = ${ParseUser.getCurrentUser().username}", Toast.LENGTH_SHORT).show()
//                Log.d("MYTAG" , "Userlist = $objects")
//                Toast.makeText(this, "Userlist = $objects", Toast.LENGTH_SHORT).show()
            }else{
//                Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initRecyclerView(users : List<String>){
        binding.recyclerView.apply {
            adapter = ListAdapter(users)
            layoutManager = LinearLayoutManager(this@UlistActivity)
        }
    }
}