package com.example.belajartodolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.belajartodolist.api.RetrofitHelper
import com.example.belajartodolist.api.TodoApi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {
    lateinit var labelHeader : TextView
    lateinit var listTodo : ListView

    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVoeHRpb2d0em1pa3plcW13d2hxIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NjU3MTM0MDAsImV4cCI6MTk4MTI4OTQwMH0.G7o5FCBDElyJsgADZ8UlTSPGTlyfG5pBwfqWb0LkrWA"
    val token = "Bearer $apiKey"

    val Items = ArrayList<Model>()
    val todoApi = RetrofitHelper.getInstance().create(TodoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        labelHeader = findViewById(R.id.label_header)
        listTodo = findViewById(R.id.list_todo)

        var result = intent.getStringExtra("result")
        labelHeader.text = "What's up, $result?"

        CoroutineScope(Dispatchers.Main).launch {
            val response = todoApi.get(token=token, apiKey=apiKey)

            response.body()?.forEach {
                Items.add(
                    Model(
                        Id=it.id,
                        Title=it.title,
                        Description=it.description
                    )
                )
            }

            setList(Items)
        }

        listTodo.setOnItemClickListener { adapterView, view, position, id ->
            val item = adapterView.getItemAtPosition(position) as Model
            val title = item.Title

            Toast.makeText(
                applicationContext,
                "Kamu memilih $title",
                Toast.LENGTH_SHORT
            ).show()
        }
        }

        fun setList(Items: ArrayList<Model>) {
            val adapter = TodoAdapter(this, R.layout.todo_item, Items)
            listTodo.adapter = adapter
        }
    }