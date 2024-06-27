package com.example.zzantechapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ZzanMain : AppCompatActivity() {

    private lateinit var targetText: TextView
    private lateinit var remainingText: TextView
    private lateinit var settingButton: Button
    private lateinit var plusButton: Button
    private lateinit var calendarButton: Button
    private lateinit var listView: ListView

    private var dailyGoal: Int = 0
    private var remainingAmount: Int = 0
    private val expenses = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    companion object {
        private const val TAG = "ZzanMain"
        const val REQUEST_CODE_SET_GOAL = 1
        const val REQUEST_CODE_ADD_EXPENSE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zzan_main)

        targetText = findViewById(R.id.target_text)
        remainingText = findViewById(R.id.textView2)
        settingButton = findViewById(R.id.setting_button)
        plusButton = findViewById(R.id.plus_button)
        calendarButton = findViewById(R.id.calendar_button)
        listView = findViewById(R.id.list)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, expenses)
        listView.adapter = adapter

        settingButton.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivityForResult(intent, REQUEST_CODE_SET_GOAL)
        }

        plusButton.setOnClickListener {
            val intent = Intent(this, AddList::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_EXPENSE)
        }

        calendarButton.setOnClickListener {
            saveCurrentStatus()
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        // Intent에서 goalAmount 값 받기
        val goalAmount = intent.getIntExtra("goalAmount", 0)
        if (goalAmount > 0) {
            dailyGoal = goalAmount
            remainingAmount = goalAmount
            targetText.text = "오늘의 목표 소비 금액은 $goalAmount 원 입니다!"

            updateRemainingAmount()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_SET_GOAL -> {
                    val goalAmount = data?.getIntExtra("goalAmount", 0) ?: 0
                    Log.d(TAG, "Received goal amount: $goalAmount")
                    dailyGoal = goalAmount
                    remainingAmount = goalAmount
                    targetText.text = "오늘의 목표 소비 금액은 $goalAmount 원 입니다!"
                    updateRemainingAmount()
                }
                REQUEST_CODE_ADD_EXPENSE -> {
                    val description = data?.getStringExtra("description") ?: ""
                    val amount = data?.getIntExtra("amount", 0) ?: 0

                    val expense = "$description / 금액: $amount 원"
                    expenses.add(expense)
                    remainingAmount -= amount
                    adapter.notifyDataSetChanged()
                    updateRemainingAmount()

                    // 목표 달성 여부 저장
                    saveCurrentStatus()

                }
            }
        }
    }

    private fun saveCurrentStatus() {
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dbHelper = DBHelper(this)
        val status = if (remainingAmount >= 0) "O" else "X"
        val usedAmount = dailyGoal - remainingAmount
        dbHelper.insertOrUpdateStatus(date, status, dailyGoal, usedAmount)
        Log.d(TAG, "Saved status for date $date: Status=$status, Goal=$dailyGoal, Used=$usedAmount")
    }

    private fun updateRemainingAmount() {
        if (remainingAmount >= 0) {
            remainingText.text = "현재 $remainingAmount 원 남았습니다."
        } else {
            val remainingAmountMinus = -(remainingAmount)
            remainingText.text = "이미 $remainingAmountMinus 원을 초과하셨습니다."
        }
    }
}
