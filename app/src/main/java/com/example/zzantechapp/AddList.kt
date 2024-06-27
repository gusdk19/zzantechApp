package com.example.zzantechapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddList : AppCompatActivity() {
    private lateinit var etDescription: EditText
    private lateinit var etAmount: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_list)

        etDescription = findViewById(R.id.edit_contents)
        etAmount = findViewById(R.id.edit_amount)
        val btnAdd = findViewById<Button>(R.id.button_add)

        btnAdd.setOnClickListener {
            val description = etDescription.text.toString()
            val amount = etAmount.text.toString().toInt()

            val resultIntent = Intent()
            resultIntent.putExtra("description", description)
            resultIntent.putExtra("amount", amount)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}