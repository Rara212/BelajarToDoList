package com.example.belajartodolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.belajartodolist.api.RetrofitHelper
import com.example.belajartodolist.api.TodoApi
import com.example.belajartodolist.api.TodoData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateTodo : AppCompatActivity() {
    lateinit var etTitle :  EditText
    lateinit var etDescription : EditText
    lateinit var btnUpdate : Button

    lateinit var id : String
    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVoeHRpb2d0em1pa3plcW13d2hxIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NjU3MTM0MDAsImV4cCI6MTk4MTI4OTQwMH0.G7o5FCBDElyJsgADZ8UlTSPGTlyfG5pBwfqWb0LkrWA"
    val token = "Bearer $apiKey"

    val todoApi = RetrofitHelper.getInstance().create(TodoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_todo)

        etTitle = findViewById(R.id.et_title_update_todo)
        etDescription = findViewById(R.id.et_description_update_todo)
        btnUpdate = findViewById(R.id.btn_update_todo)

        id = intent.getStringExtra("id").toString()

        etTitle.setText(intent.getStringExtra("title").toString())
        etDescription.setText(intent.getStringExtra("description").toString())

        btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val data = TodoData(title = etTitle.text.toString(), description = etDescription.text.toString())
                val response = todoApi.update(token = token, apiKey = apiKey, idQuery = "eq.$id", todoData = data)

                Toast.makeText(
                    applicationContext,
                    "Berhasil merubah Todo!",
                    Toast.LENGTH_SHORT
                ).show()

                finish()
            }
        }
    }
}