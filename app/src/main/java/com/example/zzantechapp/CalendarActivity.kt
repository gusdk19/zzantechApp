package com.example.zzantechapp

import android.os.Bundle
import android.util.Log
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CalendarActivity : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var statusTextView: TextView
    private lateinit var dbHelper: DBHelper

    companion object {
        private const val TAG = "CalendarActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        calendarView = findViewById(R.id.calendar_view)
        statusTextView = findViewById(R.id.status_text_view)
        dbHelper = DBHelper(this)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val statusInfo = dbHelper.getStatus(selectedDate)
            if (statusInfo == null) {
                statusTextView.text = "작성한 이력이 없습니다."
                Log.d(TAG, "No status found for date $selectedDate")
            } else {
                val (status, goalAmount, usedAmount) = statusInfo
                statusTextView.text = if (status == "O") {
                    "목표 달성: O\n목표 금액: $goalAmount 원\n사용한 금액: $usedAmount 원"
                } else {
                    "목표 달성: X\n목표 금액: $goalAmount 원\n사용한 금액: $usedAmount 원"
                }
                Log.d(TAG, "Retrieved status for date $selectedDate: Status=$status, Goal=$goalAmount, Used=$usedAmount")
            }
        }
    }
}
