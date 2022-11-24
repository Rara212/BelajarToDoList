package com.example.belajartodolist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.belajartodolist.api.RetrofitHelper
import com.example.belajartodolist.api.TodoApi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {
    lateinit var labelHeader : TextView
    lateinit var listTodo : ListView

    lateinit var btnCreateTodo : Button

    val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InVoeHRpb2d0em1pa3plcW13d2hxIiwicm9sZSI6ImFub24iLCJpYXQiOjE2NjU3MTM0MDAsImV4cCI6MTk4MTI4OTQwMH0.G7o5FCBDElyJsgADZ8UlTSPGTlyfG5pBwfqWb0LkrWA"
    val token = "Bearer $apiKey"

    var Items = ArrayList<Model>()
    val todoApi = RetrofitHelper.getInstance().create(TodoApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        labelHeader = findViewById(R.id.label_header)
        listTodo = findViewById(R.id.list_todo)
        btnCreateTodo = findViewById(R.id.btn_create_todo_activity_second)

        val result = intent.getStringExtra("result")
        labelHeader.text = "What's up, $result?"

        getItem()

       /* CoroutineScope(Dispatchers.Main).launch {
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
        }*/

        listTodo.setOnItemClickListener { adapterView, view, position, id ->
            val item = adapterView.getItemAtPosition(position) as Model
            val intent = Intent(this, UpdateTodo::class.java)
            intent.putExtra("id", item.Id)
            intent.putExtra("title", item.Title)
            intent.putExtra("description", item.Description)
            startActivity(intent)
        }

        listTodo.setOnItemLongClickListener { adapterView, view, position, id ->
            val item = adapterView.getItemAtPosition(position) as Model

            val builder = AlertDialog.Builder(this)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val id = item.Id.toString()
                    var queryId = "eq.$id"
                    deleteItem(queryId)

                    var title = item.Title.toString()
                    Toast.makeText(
                        applicationContext,
                        "Berhasil menghapus todo: $title",
                        Toast.LENGTH_SHORT
                    ).show()

                    getItem()
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }

            val alert = builder.create()
            alert.show()

            return@setOnItemLongClickListener true
        }

        btnCreateTodo.setOnClickListener {
            val intent = Intent(this, CreateTodo::class.java)
            startActivity(intent)
        }
        }

        fun setList(Items: ArrayList<Model>) {
            val adapter = TodoAdapter(this, R.layout.todo_item, Items)
            listTodo.adapter = adapter
        }

        fun deleteItem(id: String) {
            CoroutineScope(Dispatchers.Main).launch {
                todoApi.delete(token=token, apiKey=apiKey, idQuery=id)
            }
        }

        fun getItem() {
            CoroutineScope(Dispatchers.Main).launch {
                val response = todoApi.get(token=token, apiKey=apiKey)
                Items = ArrayList<Model>()
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
        }

        override fun onResume() {
            super.onResume()

            getItem()
        }
    }